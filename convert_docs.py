
from pathlib import Path
from docx import Document
from docx.text.paragraph import Paragraph
from docx.table import Table
import subprocess
import sys

# Raiz del repositorio
ROOT = Path(__file__).resolve().parent

# Documentacion original y destino
DOCS_DIR = ROOT / "documentacion"
OUTPUT_DIR = DOCS_DIR / "converted"


def clean(text):
    return " ".join(text.split()).replace("|", r"\|")


def convert_docx(source, destination):
    doc = Document(source)
    lines = [f"# {source.stem}", ""]

    # Recorre parrafos y tablas en su orden original
    for element in doc.element.body.iterchildren():
        tag = element.tag.split("}")[-1]

        if tag == "p":
            paragraph = Paragraph(element, doc)
            text = clean(paragraph.text)

            if not text:
                continue

            style = paragraph.style.name if paragraph.style else ""

            if style.startswith("Heading"):
                try:
                    level = int(style.replace("Heading", "").strip())
                    level = max(1, min(level, 6))
                except ValueError:
                    level = 2

                lines.extend(["#" * level + " " + text, ""])
            else:
                lines.extend([text, ""])

        elif tag == "tbl":
            table = Table(element, doc)
            rows = []

            for row in table.rows:
                rows.append([clean(cell.text) for cell in row.cells])

            if rows:
                columns = max(len(row) for row in rows)

                header = rows[0] + [""] * (columns - len(rows[0]))
                lines.append("| " + " | ".join(header) + " |")
                lines.append("| " + " | ".join(["---"] * columns) + " |")

                for row in rows[1:]:
                    row += [""] * (columns - len(row))
                    lines.append("| " + " | ".join(row) + " |")

                lines.append("")

    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text("\n".join(lines), encoding="utf-8")


def main():
    if not DOCS_DIR.exists():
        print(f"ERROR: No existe {DOCS_DIR}")
        sys.exit(1)

    # Busca todos los DOCX en cualquier nivel de documentacion
    # Excluye la carpeta converted y los archivos temporales de Word
    files = [
        path for path in DOCS_DIR.rglob("*")
        if path.is_file()
        and path.suffix.lower() == ".docx"
        and OUTPUT_DIR not in path.parents
        and not path.name.startswith("~$")
    ]

    if not files:
        print("No se encontraron documentos DOCX.")
        return

    print(f"Documentos encontrados: {len(files)}\n")

    success = []
    errors = []

    for index, source in enumerate(files, 1):

        # Conserva toda la estructura de carpetas relativa
        relative_path = source.relative_to(DOCS_DIR)

        # Cambia documentacion/.../archivo.docx
        # por documentacion/converted/.../archivo.md
        destination = (OUTPUT_DIR / relative_path).with_suffix(".md")

        print(f"[{index}/{len(files)}] {relative_path}")

        try:
            convert_docx(source, destination)
            success.append(destination)
            print("  OK")

        except Exception as error:
            errors.append((source, str(error)))
            print(f"  ERROR: {error}")

    print("\n========== RESUMEN ==========")
    print(f"Encontrados: {len(files)}")
    print(f"Convertidos: {len(success)}")
    print(f"Errores:     {len(errors)}")

    if errors:
        print("\nDocumentos con errores:")
        for path, error in errors:
            print(f"- {path.relative_to(ROOT)}: {error}")

    if success:
        result = subprocess.run(
            ["git", "rev-parse", "--show-toplevel"],
            cwd=ROOT,
            capture_output=True,
            text=True
        )

        if result.returncode == 0:
            subprocess.run(
                ["git", "add", "documentacion/converted/"],
                cwd=ROOT,
                check=True
            )

            print("\nArchivos preparados para Git.")
            print("Revisa los cambios con: git status")

        else:
            print("\nNo se detecto un repositorio Git.")

    print(f"\nResultados guardados en: {OUTPUT_DIR}")


if __name__ == "__main__":
    main()
