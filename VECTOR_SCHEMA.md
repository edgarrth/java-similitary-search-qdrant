# Payment risk vector schema

# Versión

`payment-risk-vector-v2`

# Objetivo

Este schema transforma pagos y perfiles históricos de comercios en un vector de 32 dimensiones para ejecutar similarity search en Qdrant usando distancia coseno.

El diseño evita hashing genérico y usa dimensiones explícitas. Esto permite explicar por qué dos pagos son similares, versionar cambios del vector y migrar colecciones en Qdrant cuando el contrato cambia.

# Dimensiones

| Índice | Dimensión | Tipo | Descripción |
|---:|---|---|---|
| 0 | amount_log_norm | Numérica | Monto con log-normalización y cap de 20,000. |
| 1 | amount_micro | Bucket | Monto <= 10. |
| 2 | amount_low | Bucket | Monto > 10 y <= 100. |
| 3 | amount_medium | Bucket | Monto > 100 y <= 1,000. |
| 4 | amount_high | Bucket | Monto > 1,000. |
| 5 | card_local | Señal | Pago con tarjeta no internacional. |
| 6 | card_international | Señal | Pago con tarjeta internacional. |
| 7 | new_device | Señal | Pago desde dispositivo nuevo. |
| 8 | previous_chargebacks | Numérica | Contracargos previos normalizados con cap de 5. |
| 9 | historical_chargeback_pressure | Numérica | Presión de contracargos históricos o proxy del pago actual. |
| 10 | historical_fraud_pressure | Numérica | Presión de fraude histórico o proxy del pago actual. |
| 11 | method_card | Categórica | Método de pago CARD. |
| 12 | method_wallet | Categórica | Método de pago WALLET. |
| 13 | method_transfer | Categórica | Método de pago TRANSFER. |
| 14 | method_qr | Categórica | Método de pago QR. |
| 15 | channel_pos | Categórica | Canal POS / físico. |
| 16 | channel_ecommerce | Categórica | Canal ecommerce / online. |
| 17 | channel_mobile | Categórica | Canal móvil / app / wallet. |
| 18 | channel_api | Categórica | Canal API / open banking / pago inmediato. |
| 19 | country_pe | Categórica | Operación en Perú. |
| 20 | country_latam | Categórica | Operación en país LATAM distinto de Perú. |
| 21 | country_other | Categórica | Operación fuera de LATAM. |
| 22 | mcc_food_grocery | Categórica | Comida, restaurante o grocery. |
| 23 | mcc_pharmacy_health | Categórica | Farmacia o salud. |
| 24 | mcc_retail_electronics | Categórica | Retail o electrónica. |
| 25 | mcc_travel_ticketing | Categórica | Viajes, hoteles, tickets o eventos. |
| 26 | mcc_transport | Categórica | Transporte. |
| 27 | mcc_digital_goods | Categórica | Bienes digitales o servicios digitales. |
| 28 | risk_intensity | Numérica | Intensidad de riesgo consolidada. |
| 29 | action_approve_prior | Prior | Perfil o pago orientado a aprobación. |
| 30 | action_review_prior | Prior | Perfil o pago orientado a revisión. |
| 31 | action_decline_prior | Prior | Perfil o pago orientado a rechazo. |

# Reglas importantes

- Todos los vectores se normalizan con L2 antes de enviarse a Qdrant.
- Qdrant está configurado con distancia `Cosine`.
- Cualquier cambio incompatible en este schema debe crear una nueva colección.
- La colección por defecto de esta versión es `merchant_risk_profiles_v2`.
- Para una implementación productiva, este schema podría alimentarse desde una feature-store y combinarse con embeddings de texto para descriptores de comercios.
