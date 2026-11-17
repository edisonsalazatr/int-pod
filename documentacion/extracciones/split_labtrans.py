import csv
from pathlib import Path

source = Path("LABTRANS Ottobre.csv")
output_dir = Path("labtrans_parts")
output_dir.mkdir(exist_ok=True)

with source.open("r", encoding="utf-8-sig", newline="") as f:
    sample = f.read(8192)
    f.seek(0)
    dialect = csv.Sniffer().sniff(sample)
    reader = csv.reader(f, dialect)
    header = next(reader)

    part = 1
    rows = []
    size = 0
    limit = 2 * 1024 * 1024

    def save():
        global part
        path = output_dir / f"LABTRANS_part_{part:03}.csv"
        with path.open("w", encoding="utf-8-sig", newline="") as out:
            writer = csv.writer(out, dialect)
            writer.writerow(header)
            writer.writerows(rows)
        print(f"{path}: {path.stat().st_size:,} bytes")
        part += 1

    for row in reader:
        buffer = __import__("io").StringIO()
        csv.writer(buffer, dialect).writerow(row)
        row_size = len(buffer.getvalue().encode("utf-8"))

        if rows and size + row_size > limit:
            save()
            rows = []
            size = 0

        rows.append(row)
        size += row_size

    if rows:
        save()

print("Division completada.")
