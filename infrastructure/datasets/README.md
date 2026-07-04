# Dataset de perfiles de riesgo para Payment Similarity Search

# Contenido

Esta carpeta contiene datos de prueba para precargar perfiles de riesgo de comercios en la PoC.

| Archivo | Descripción |
|---|---|
| `merchant-risk-profiles.json` | Perfiles históricos de comercios y patrones de pago. |
| `seed_profiles.py` | Script Python que invoca la API del microservicio para indexar perfiles en Qdrant. |

# Prerrequisitos

1. Qdrant levantado:

```bash
docker compose -f infrastructure/docker-compose.yml up -d
```

2. Microservicio ejecutándose:

```bash
mvn spring-boot:run
```

# Precargar datos

Desde la raíz del proyecto:

```bash
python3 infrastructure/datasets/seed_profiles.py
```

El script llama a:

```text
POST http://localhost:8080/api/v1/merchant-risk-profiles
```

El microservicio calcula el vector con `FeatureHashingPaymentEmbeddingAdapter` y lo guarda en Qdrant.

# Validar datos en Qdrant

```bash
curl http://localhost:6333/collections/merchant_risk_profiles
```

Para inspeccionar puntos:

```bash
curl -X POST http://localhost:6333/collections/merchant_risk_profiles/points/scroll \
  -H 'Content-Type: application/json' \
  -d '{"limit": 5, "with_payload": true, "with_vector": false}'
```
