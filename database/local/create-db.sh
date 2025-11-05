# 1. Crear un volumen para persistir los datos
podman volume create postgres_data

# 2. Ejecutar el contenedor de PostgreSQL
podman run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=neuralbank123! \
  -e POSTGRES_USER=neuralbank \
  -e POSTGRES_DB=neuralbank \
  -v postgres_data:/var/lib/postgresql/data \
  -v ./.:/scripts:Z \
  -p 5432:5432 \
  docker.io/library/postgres:16



# 3. Esperar a que inicie
sleep 10

# 4. Importar el script
podman exec -it postgres psql -U neuralbank -d neuralbank -f /scripts/neuralbank_schema_international.sql
podman exec -it postgres psql -U neuralbank -d neuralbank -f /scripts/neuralbank_data_international.sql







# 3. Verificar que el contenedor está corriendo
podman ps




# 4. Ver los logs del contenedor (opcional)
#podman logs postgres


