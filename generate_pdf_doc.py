from pathlib import Path
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib.units import cm

project_root = Path(__file__).resolve().parent
source = project_root / "PROJECT_DOCUMENTATION.md"
out_pdf = project_root / "PROJECT_DOCUMENTATION.pdf"

text = source.read_text(encoding="utf-8")
lines = text.splitlines()

styles = getSampleStyleSheet()
normal = styles["BodyText"]
normal.fontName = "Helvetica"
normal.fontSize = 10
normal.leading = 14

h1 = styles["Heading1"]
h2 = styles["Heading2"]

story = []
for line in lines:
    clean = line.strip()
    if not clean:
        story.append(Spacer(1, 0.2 * cm))
        continue

    safe = clean.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

    if safe.startswith("# "):
        story.append(Paragraph(safe[2:], h1))
    elif safe.startswith("## "):
        story.append(Spacer(1, 0.1 * cm))
        story.append(Paragraph(safe[3:], h2))
    elif safe.startswith("- "):
        story.append(Paragraph(f"• {safe[2:]}", normal))
    elif safe[:2].isdigit() and safe[1:3] == ". ":
        story.append(Paragraph(safe, normal))
    else:
        story.append(Paragraph(safe, normal))


doc = SimpleDocTemplate(
    str(out_pdf),
    pagesize=A4,
    leftMargin=2 * cm,
    rightMargin=2 * cm,
    topMargin=2 * cm,
    bottomMargin=2 * cm,
)
doc.build(story)

print(f"Generated: {out_pdf}")
