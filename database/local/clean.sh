# 1. Detener y eliminar el contenedor
podman stop postgres
podman rm postgres

# 2. ELIMINAR el volumen antiguo
podman volume rm postgres_data