from pathlib import Path
import gzip

REPLACEMENTS = {
    b"minecraft:movement_speed": b"minecraft:generic.movement_speed",
    b"minecraft:follow_range": b"minecraft:generic.follow_range",
    b"minecraft:armor": b"minecraft:generic.armor",
}

def patch_nbt(data):
    try:
        raw = gzip.decompress(data)
    except OSError:
        raw = data
        compressed = False
    else:
        compressed = True

    changed = 0
    for old, new in REPLACEMENTS.items():
        offset = 0
        while True:
            index = raw.find(old, offset)
            if index < 0:
                break
            if index >= 2 and int.from_bytes(raw[index - 2:index], "big") == len(old):
                raw = raw[:index - 2] + len(new).to_bytes(2, "big") + new + raw[index + len(old):]
                changed += 1
                offset = index + len(new)
            else:
                offset = index + len(old)

    if not changed:
        return data, 0
    return (gzip.compress(raw, mtime=0) if compressed else raw), changed

total = 0
for path in Path("src/main").rglob("*.nbt"):
    original = path.read_bytes()
    patched, count = patch_nbt(original)
    if count:
        path.write_bytes(patched)
        print(f"{path}: fixed {count} attribute IDs")
        total += count

print(f"Fixed {total} legacy attribute IDs.")
