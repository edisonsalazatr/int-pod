from pathlib import Path
from docx import Document
import re

# ============================================================
# CONFIGURACIÓN
# ============================================================

SOURCE_ROOT = Path(
    r"C:\Users\fesalazar\OneDrive - Indra\Shortcuts\Grp_T_Maximo_Terna - EAM"
)

DEST_ROOT = Path(
    r"C:\Users\fesalazar\OneDrive - Indra\Progetti\Terna\Integrazioni\Codice POD\pod\documentacion\converted"
)

# Longitud máxima del nombre del archivo .md
MAX_FILENAME_LENGTH = 100


# ============================================================
# FUNCIONES
# ============================================================

def sanitize_filename(filename: str) -> str:
    """
    Elimina caracteres problemáticos para Windows
    y limita la longitud del nombre.
    """
    # Caracteres no permitidos en Windows
    filename = re.sub(r'[<>:"/\\|?*]', '_', filename)

    # Elimina espacios/puntos al final
    filename = filename.rstrip(" .")

    if len(filename) > MAX_FILENAME_LENGTH:
        filename = filename[:MAX_FILENAME_LENGTH].rstrip()

    return filename


def paragraph_to_markdown(paragraph):
    """
    Convierte un párrafo DOCX a Markdown básico.
    """

    text = paragraph.text.strip()

    if not text:
        return ""

    style = paragraph.style.name.lower() if paragraph.style else ""

    # Títulos
    if "heading 1" in style or "titolo 1" in style:
        return f"# {text}"

    if "heading 2" in style or "titolo 2" in style:
        return f"## {text}"

    if "heading 3" in style or "titolo 3" in style:
        return f"### {text}"

    if "heading 4" in style or "titolo 4" in style:
        return f"#### {text}"

    # Listas
    if "list bullet" in style or "elenco puntato" in style:
        return f"- {text}"

    if "list number" in style or "elenco numerato" in style:
        return f"1. {text}"

    return text


def table_to_markdown(table):
    """
    Convierte una tabla DOCX a Markdown.
    """

    rows = []

    for row in table.rows:
        cells = []

        for cell in row.cells:
            text = cell.text.strip()
            text = text.replace("|", "\\|")
            text = text.replace("\n", "<br>")

            cells.append(text)

        rows.append(cells)

    if not rows:
        return ""

    # Número máximo de columnas
    max_columns = max(len(row) for row in rows)

    # Normalizar filas
    for row in rows:
        while len(row) < max_columns:
            row.append("")

    markdown = []

    # Cabecera
    header = rows[0]

    markdown.append(
        "| " + " | ".join(header) + " |"
    )

    markdown.append(
        "| " + " | ".join(["---"] * max_columns) + " |"
    )

    # Resto
    for row in rows[1:]:
        markdown.append(
            "| " + " | ".join(row) + " |"
        )

    return "\n".join(markdown)


def convert_docx_to_markdown(source_file, destination_file):
    """
    Convierte un DOCX completo a Markdown.
    """

    document = Document(source_file)

    markdown = []

    # Recorremos los elementos del documento
    # manteniendo párrafos y tablas en el orden original.
    for element in document.element.body:

        if element.tag.endswith("}p"):
            for paragraph in document.paragraphs:
                if paragraph._p is element:
                    result = paragraph_to_markdown(paragraph)

                    if result:
                        markdown.append(result)

                    break

        elif element.tag.endswith("}tbl"):
            for table in document.tables:
                if table._tbl is element:
                    result = table_to_markdown(table)

                    if result:
                        markdown.append(result)

                    break

    # Separación entre elementos
    content = "\n\n".join(markdown)

    destination_file.parent.mkdir(parents=True, exist_ok=True)

    # Sobrescribe automáticamente
    destination_file.write_text(
        content,
        encoding="utf-8"
    )


# ============================================================
# PROCESO PRINCIPAL
# ============================================================

def main():

    if not SOURCE_ROOT.exists():
        print()
        print("ERROR: No existe la carpeta origen:")
        print(SOURCE_ROOT)
        return

    DEST_ROOT.mkdir(parents=True, exist_ok=True)

    docx_files = [
        file
        for file in SOURCE_ROOT.rglob("*")
        if file.is_file() and file.suffix.lower() == ".docx"
    ]

    print()
    print("=" * 70)
    print("CONVERSIÓN DOCX -> MARKDOWN")
    print("=" * 70)
    print()
    print(f"Origen : {SOURCE_ROOT}")
    print(f"Destino: {DEST_ROOT}")
    print()
    print(f"Documentos encontrados: {len(docx_files)}")
    print()

    converted = 0
    errors = 0

    for source_file in docx_files:

        # Ruta relativa respecto al directorio origen
        relative_path = source_file.relative_to(SOURCE_ROOT)

        # Carpeta destino correspondiente
        destination_directory = (
            DEST_ROOT / relative_path.parent
        )

        # Nombre .md
        filename = sanitize_filename(
            source_file.stem
        ) + ".md"

        destination_file = (
            destination_directory / filename
        )

        print(f"[CONVIRTIENDO]")
        print(f"  DOCX: {source_file}")
        print(f"  MD  : {destination_file}")

        try:

            convert_docx_to_markdown(
                source_file,
                destination_file
            )

            converted += 1
            print("  OK")
            print()

        except Exception as e:

            errors += 1

            print("  ERROR")
            print(f"  {type(e).__name__}: {e}")
            print()

    print("=" * 70)
    print("RESULTADO")
    print("=" * 70)
    print(f"Encontrados : {len(docx_files)}")
    print(f"Convertidos : {converted}")
    print(f"Errores     : {errors}")
    print()


if __name__ == "__main__":
    main()