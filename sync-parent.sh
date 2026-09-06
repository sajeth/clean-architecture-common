#!/usr/bin/env bash
# Keep pom.xml <parent> on the latest published m2-commons-parent.
#
# Maven cannot use LATEST/RELEASE (or ranges) for parent POMs — the version must
# be a concrete string. This script is the force-latest mechanism for CI + local.
#
# Usage:
#   GH_PACKAGES_TOKEN=<token> GITHUB_ACTOR=<user> ./sync-parent.sh           # apply
#   GH_PACKAGES_TOKEN=<token> GITHUB_ACTOR=<user> ./sync-parent.sh --check   # exit 1 if behind
#   GH_PACKAGES_TOKEN=<token> GITHUB_ACTOR=<user> ./sync-parent.sh --print   # print latest only
#
# Auth: same as Maven builds (GH_PACKAGES_TOKEN + GITHUB_ACTOR). Prefer a token
# with read:packages; repo-scoped tokens often work for packages linked to the repo.

set -euo pipefail

MODE=apply
case "${1:-}" in
  --check) MODE=check ;;
  --print) MODE=print ;;
  --apply|"") MODE=apply ;;
  -h|--help)
    sed -n '2,16p' "$0"
    exit 0
    ;;
  *)
    echo "unknown option: $1 (use --check, --print, or --apply)" >&2
    exit 2
    ;;
esac

: "${GH_PACKAGES_TOKEN:?Set GH_PACKAGES_TOKEN}"
: "${GITHUB_ACTOR:?Set GITHUB_ACTOR}"

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

METADATA_URL="https://maven.pkg.github.com/sajeth/m2-java-parent/io/github/sajeth/m2-commons-parent/maven-metadata.xml"
SETTINGS=()
if [[ -f .github/maven-settings.xml ]]; then
  SETTINGS=(--settings .github/maven-settings.xml)
fi

current_parent_version() {
  python3 - <<'PY'
import re
text = open("pom.xml").read()
m = re.search(r"<parent>[\s\S]*?<version>([^<]+)</version>", text)
if not m:
    raise SystemExit("error: could not find <parent><version> in pom.xml")
print(m.group(1))
PY
}

fetch_latest_via_metadata() {
  local meta
  if ! meta=$(curl -sf -u "${GITHUB_ACTOR}:${GH_PACKAGES_TOKEN}" "$METADATA_URL"); then
    return 1
  fi
  python3 -c '
import re, sys
text = sys.stdin.read()
for tag in ("release", "latest"):
    m = re.search(rf"<{tag}>([^<]+)</{tag}>", text)
    if m:
        print(m.group(1))
        raise SystemExit(0)
raise SystemExit("error: could not parse version from maven-metadata.xml")
' <<<"$meta"
}

# Fallback when curl lacks read:packages: let Maven resolve parent updates.
fetch_latest_via_maven() {
  local out
  out=$(mvn --no-transfer-progress -q \
    org.codehaus.mojo:versions-maven-plugin:2.17.1:display-parent-updates \
    -DallowMajorUpdates=true \
    -DallowMinorUpdates=true \
    -DallowIncrementalUpdates=true \
    "${SETTINGS[@]}" \
    -DforceStdout 2>/dev/null || true)

  # Typical line: "  io.github.sajeth:m2-commons-parent ... 2026.9.1 -> 2026.10.1"
  python3 -c '
import re, sys
text = sys.stdin.read()
m = re.search(r"m2-commons-parent\S*\s+(\S+)\s+->\s+(\S+)", text)
if m:
    print(m.group(2))
    raise SystemExit(0)
# No arrow means already latest (or plugin printed nothing useful).
m = re.search(r"<parent>[\s\S]*?<version>([^<]+)</version>", open("pom.xml").read())
print(m.group(1) if m else "")
' <<<"$out"
}

apply_parent_version() {
  local latest="$1"
  python3 - "$latest" <<'PY'
import re, sys
latest = sys.argv[1]
content = open("pom.xml").read()
updated, n = re.subn(
    r"(<parent>[\s\S]*?<version>)([^<]+)(</version>[\s\S]*?</parent>)",
    lambda m: m.group(1) + latest + m.group(3),
    content,
    count=1,
)
if n != 1:
    raise SystemExit("error: failed to rewrite parent version in pom.xml")
open("pom.xml", "w").write(updated)
current = re.search(r"<parent>[\s\S]*?<version>([^<]+)</version>", content).group(1)
if current == latest:
    print(f"m2-commons-parent already at {latest}")
else:
    print(f"m2-commons-parent updated: {current} → {latest}")
PY
}

CURRENT="$(current_parent_version)"
LATEST=""
if LATEST=$(fetch_latest_via_metadata); then
  :
elif LATEST=$(fetch_latest_via_maven) && [[ -n "$LATEST" ]]; then
  echo "note: resolved latest parent via Maven (metadata API unavailable)" >&2
else
  echo "error: could not determine latest m2-commons-parent version" >&2
  exit 1
fi

case "$MODE" in
  print)
    echo "$LATEST"
    ;;
  check)
    if [[ "$CURRENT" == "$LATEST" ]]; then
      echo "m2-commons-parent is current ($CURRENT)"
      exit 0
    fi
    echo "m2-commons-parent is behind: pom=$CURRENT latest=$LATEST" >&2
    echo "Run: GH_PACKAGES_TOKEN=… GITHUB_ACTOR=… ./sync-parent.sh" >&2
    exit 1
    ;;
  apply)
    apply_parent_version "$LATEST"
    ;;
esac
