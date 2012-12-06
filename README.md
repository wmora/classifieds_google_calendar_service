classifieds_google_calendar_service
===================================

Servicio de Google Calendar para circuito clasificados

Para lograr la autenticación con el Google Calendar API, es necesario registrar la aplicación en https://code.google.com/apis/console. Antes de registrar la aplicación, se debe activar la opción de Calendar API en la lista de servicios a utilizar. En la configuración de API Access, generar un client ID para aplicaciones instaladas.

Una vez generado el client ID y client secret, se debe guardar en el archivo src/main/resources/client_secrets.json con el siguiente formato:

{
    "installed": {
        "client_id": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "client_secret": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    }
}
