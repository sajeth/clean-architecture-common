import re, sys, os

content = open('pom.xml').read()
latest_parent = sys.argv[1] if len(sys.argv) > 1 and sys.argv[1] else ""

if latest_parent:
    content = re.sub(
        r'(<parent>[\s\S]*?<version>)([^<]+)(</version>[\s\S]*?</parent>)',
        lambda m: m.group(1) + latest_parent + m.group(3),
        content
    )
    print(f"Parent updated to {latest_parent}")

m = re.search(r'<version>(\d+)\.(\d+)\.(\d+)</version>', content)
if not m:
    print("Could not find project version", file=sys.stderr)
    sys.exit(1)

new_version = f"{m.group(1)}.{m.group(2)}.{int(m.group(3)) + 1}"
content = re.sub(r'<version>\d+\.\d+\.\d+</version>', f'<version>{new_version}</version>', content, count=1)
print(f"Project version bumped to {new_version}")

open('pom.xml', 'w').write(content)

github_env = os.environ.get('GITHUB_ENV')
if github_env:
    with open(github_env, 'a') as f:
        f.write(f'NEW_VERSION={new_version}\n')
