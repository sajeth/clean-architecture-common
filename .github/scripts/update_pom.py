import os
import re
import sys


def parent_version(pom: str) -> str:
    match = re.search(r"<parent>[\s\S]*?<version>([^<]+)</version>", pom)
    return match.group(1) if match else ""


content = open("pom.xml").read()
latest_parent = sys.argv[1] if len(sys.argv) > 1 and sys.argv[1] else ""
parent_changed = False

if latest_parent:
    current_parent = parent_version(content)
    if current_parent == latest_parent:
        print(f"Parent already {latest_parent} — skipping version bump")
        github_env = os.environ.get("GITHUB_ENV")
        if github_env:
            with open(github_env, "a") as handle:
                handle.write("NEW_VERSION=\n")
                handle.write("PARENT_CHANGED=false\n")
        sys.exit(0)
    content = re.sub(
        r"(<parent>[\s\S]*?<version>)([^<]+)(</version>[\s\S]*?</parent>)",
        lambda match: match.group(1) + latest_parent + match.group(3),
        content,
    )
    parent_changed = True
    print(f"Parent updated to {latest_parent}")

parent_match = re.search(r"<parent>[\s\S]*?</parent>", content)
search_start = parent_match.end() if parent_match else 0

match = re.search(r"<version>(\d+)\.(\d+)\.(\d+)</version>", content[search_start:])
if not match:
    print("Could not find project version", file=sys.stderr)
    sys.exit(1)

abs_start = search_start + match.start()
abs_end = search_start + match.end()
new_version = f"{match.group(1)}.{match.group(2)}.{int(match.group(3)) + 1}"
content = content[:abs_start] + f"<version>{new_version}</version>" + content[abs_end:]
print(f"Project version bumped to {new_version}")

open("pom.xml", "w").write(content)

github_env = os.environ.get("GITHUB_ENV")
if github_env:
    with open(github_env, "a") as handle:
        handle.write(f"NEW_VERSION={new_version}\n")
        handle.write(f"PARENT_CHANGED={'true' if parent_changed else 'false'}\n")
