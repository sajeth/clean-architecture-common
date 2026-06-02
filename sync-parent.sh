#!/usr/bin/env bash
# Fetch the latest m2-commons-parent version from GitHub Packages and update pom.xml.
# Usage: GH_PACKAGES_TOKEN=<token> GITHUB_ACTOR=<user> ./sync-parent.sh

set -euo pipefail

: "${GH_PACKAGES_TOKEN:?Set GH_PACKAGES_TOKEN}"
: "${GITHUB_ACTOR:?Set GITHUB_ACTOR}"

METADATA_URL="https://maven.pkg.github.com/sajeth/m2-java-parent/io/github/sajeth/m2-commons-parent/maven-metadata.xml"

METADATA=$(curl -sf -u "${GITHUB_ACTOR}:${GH_PACKAGES_TOKEN}" "$METADATA_URL")

LATEST=$(echo "$METADATA" | grep -oP '(?<=<release>)[^<]+' | head -1)
if [ -z "$LATEST" ]; then
  LATEST=$(echo "$METADATA" | grep -oP '(?<=<latest>)[^<]+' | head -1)
fi

if [ -z "$LATEST" ]; then
  echo "error: could not parse a version from maven-metadata.xml" >&2
  exit 1
fi

python3 - "$LATEST" <<'EOF'
import re, sys
latest = sys.argv[1]
content = open('pom.xml').read()
updated = re.sub(
    r'(<parent>[\s\S]*?<version>)([^<]+)(</version>[\s\S]*?</parent>)',
    lambda m: m.group(1) + latest + m.group(3),
    content
)
open('pom.xml', 'w').write(updated)
current = re.search(r'<parent>[\s\S]*?<version>([^<]+)</version>', content).group(1)
if current == latest:
    print(f"m2-commons-parent already at {latest}")
else:
    print(f"m2-commons-parent updated: {current} → {latest}")
EOF
