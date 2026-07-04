# Build verification

# Validaciones realizadas en el entorno de generación

Se ejecutaron estas verificaciones antes de generar el ZIP:

```bash
# Validación XML/YAML/JSON
python3 - <<'PY_VALIDATION'
import json, yaml, xml.etree.ElementTree as ET
from pathlib import Path
root=Path('.')
ET.parse(root/'pom.xml')
yaml.safe_load((root/'src/main/resources/application.yml').read_text())
json.loads((root/'infrastructure/datasets/merchant-risk-profiles.json').read_text())
PY_VALIDATION

# Validación de compilación de dominio con javac disponible en el entorno
javac --release 21 -d /tmp/domain-classes $(find src/main/java/com/edgarrt/poc/paymentsimilarity/domain -name '*.java')

# Validación sintáctica de todo src/main/java con stubs mínimos para APIs externas
javac --release 21 -d /tmp/full-classes $(find /tmp/java-stubs -name '*.java') $(find src/main/java -name '*.java')
```

Resultado:

```text
static validation ok: pom.xml, application.yml, dataset.json, domain javac
stubbed full-main javac ok
```

# Limitación del entorno

No pude ejecutar un `mvn clean verify` real dentro del sandbox porque el entorno donde se generó el ZIP solo tenía JDK 21, no tenía Maven instalado y el acceso DNS para instalar paquetes falló. El proyecto queda configurado para compilar con JDK 25 y Maven mediante:

```bash
mvn clean verify
```

También se incluye `infrastructure/Dockerfile` para compilar con una imagen Maven + Eclipse Temurin 25.
