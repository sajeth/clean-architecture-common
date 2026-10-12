#!/usr/bin/env bash
# Keep pom.xml <parent> on Maven LATEST for m2-commons-parent.
#
# Usage:
#   ./sync-parent.sh           # apply (ensure LATEST)
#   ./sync-parent.sh --check   # exit 1 if not LATEST
#   ./sync-parent.sh --print   # print LATEST
#
# Auth env vars are accepted for compatibility with CI callers but unused.

set -euo pipefail

MODE=apply
case "${1:-}" in
  --check) MODE=check ;;
  --print) MODE=print ;;
  --apply|"") MODE=apply ;;
  -h|--help)
    sed -n '2,12p' "$0"
    exit 0
    ;;
  *)
    echo "unknown option: $1 (use --check, --print, or --apply)" >&2
    exit 2
    ;;
esac

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

TARGET=LATEST

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

case "$MODE" in
  print)
    echo "$TARGET"
    ;;
  check)
    if [[ "$CURRENT" == "$TARGET" ]]; then
      echo "m2-commons-parent is current ($CURRENT)"
      exit 0
    fi
    echo "m2-commons-parent is behind: pom=$CURRENT expected=$TARGET" >&2
    echo "Run: ./sync-parent.sh" >&2
    exit 1
    ;;
  apply)
    apply_parent_version "$TARGET"
    ;;
esac
