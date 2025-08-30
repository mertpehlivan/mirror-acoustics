# whatsapp-shop

WhatsApp siparişli demo e-ticaret uygulaması (Spring Boot 3, Thymeleaf, JPA, Flyway, Postgres).

## Kurulum

1. PostgreSQL başlat:

```bash
docker compose up -d
```

2. Uygulama yapılandırması: `src/main/resources/application.yml`

3. Çalıştırma:

```bash
./mvnw spring-boot:run
```

## Özellikler
- Ürün/Kategori/Kupon/OrderDraft şeması (Flyway)
- WhatsApp sipariş akışı (wa.me)
- Thymeleaf ile basit UI (Tailwind CDN)

## Not
Ödeme entegrasyonu yoktur; siparişler WhatsApp üzerinden alınır.

