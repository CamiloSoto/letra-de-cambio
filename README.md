
# Sistema de gestión de letras de cambio
Stack: Java (Spring Boot) + React

Base de datos: PostgreSQL 

Licencia: portafolio
## DESCRIPCIÓN
Este proyecto permite gestionar letras de cambio de manera digital
y física, respetando normas legales de Colombia y prácticas comerciales.
El sistema soporta:

- Creación de letras de cambio.
- Selección de girado y beneficiario (por email/documento)
- Invitación de usuarios no registrados
- Aceptación mediante firma electrónica simple
- Generación de PDF inmutable de la letra
- Registro de firma física (PDF escaneado)
- Endosos con documentos anexos
- Registro de pagos y trazabilidad completa
- Auditoría de todas las acciones

El flujo del proyecto separa claramente las responsabilidades entre:

1. USUARIO: realiza acciones de registro, aceptación, endoso y pago
2. SISTEMA: valida, registra, genera documentos, audita y protege la inmutabilidad

El sistema **no firma por los usuarios**; la firma electrónica sirve como evidencia y trazabilidad.
La firma física mantiene la validez legal del documento.

## FLUJO PRINCIPAL

1. Usuario se registra / inicia sesión
2. Usuario crea la letra de cambio
3. Selecciona girado y beneficiario por email/documento
4. Si el usuario no existe:
   - Se envía correo de invitación
5. Estado de letra: BORRADOR
6. Ususrios seleccionados en el registro aceptan electrónicamente (FIRMA ELECTRÓNICA)
   - El sistema registra FirmaElectronica con IP, fecha y hash
7. Generar PDF de la letra sin firmas (inmutable)
8. Estado: GENERADA
9. Usuario imprime y firma físicamente
10. Usuario sube el PDF firmado
    - El sistema calcula hash y registra auditoría
11. Estado: FIRMADA
12. Endoso:
    - Endosante firma electrónicamente
    - Documento anexo generado
    - Estado actualizado
13. Pago:
    - Confirmación mediante firma electrónica
    - Estado actualizado: PAGADA

## FLUJO DE REGISTRO Y VALIDACION DE USUARIO

1. USER (Cliente Web / React)
   - El usuario llena el formulario de registro con los siguientes datos:
     - name
     - email
     - password
   - Envía la información al endpoint: POST /auth/register
1. API
   - Recibe name, email y password
   - Valida si el correo ya se encuentra registrado
     - Si existe, retorna error 400 (Bad Request)
   - Si no existe:
     - Genera un UUID para verificación de correo
     - Crea el registro del usuario con:
        name
        email
        password (hash)
        verified = false
        emailCode = UUID
     - Envía un correo electrónico al usuario con el enlace de verificación (incluye el UUID)

2. REACT
   - Recibe la respuesta del backend
   - Si ocurre un error:
     - Muestra el mensaje correspondiente
   - Si el registro es exitoso:
      - Muestra mensaje indicando que debe validar su correo electrónico
3. USER
   - Abre el enlace recibido por correo
   - Completa el formulario con los siguientes datos:
      - tipo_identificacion
      - identificacion
      - apellido
      - UUID
   - Envía la información al endpoint: POST /auth/validate

1. API
   - Recibe los datos de validación
   - Valida el UUID y busca el registro asociado
- Si el UUID es válido:
    - Actualiza los datos adicionales del usuario
    - Elimina el UUID de verificación del registro
    - Actualiza el estado del usuario a:
      - verified = true
    - Envía un correo de confirmación de cuenta activada
  - Si el UUID no es válido:
    - Retorna el error correspondiente
1. REACT
   - Recibe la respuesta del backend
   - Muestra el mensaje final al usuario (éxito o error)

## ESTADOS DE LA LETRA

BORRADOR: Creada, pendiente de aceptación
GENERADA: Aceptada electrónicamente, PDF generado inmutable
FIRMADA: PDF firmado físicamente y subido
ENDOSADA: Se han realizado endosos (anexos)
PAGADA: Pago registrado y firmado electrónicamente
VENCIDA: Fecha de vencimiento alcanzada sin pago

## ENTIDADES PRINCIPALES

User: información del usuario y firma visual opcional
LetraCambio: datos económicos, partes, fechas, estado
DocumentoLetra: PDFs originales, firmados y anexos
FirmaElectronica: registro de aceptación, endoso y pago
Endoso: evento legal de endoso entre usuarios
Pago: registro de pagos y responsables
Auditoria: trazabilidad completa de acciones y cambios

## REQUERIMIENTOS

- Java 17+
- Spring Boot 3+
- PostgreSQL
- Node.js y npm (para frontend React)
- Maven
- VSCode

## INSTALACIÓN Y USO

1. Clonar el repositorio:
   git clone git@github.com:CamiloSoto/letra-de-cambio.git

2. Configurar base de datos en application.yml

3. Ejecutar backend:
   mvn spring-boot:run

4. Ejecutar frontend (desde carpeta frontend):
   npm install
   npm start

5. Acceder a la aplicación en:
   http://localhost:3000

## NOTAS IMPORTANTES

- La firma electrónica registra aceptación, endoso o pago, 
  no reemplaza la firma física
- Los documentos originales son inmutables
- Los endosos generan documentos anexos sin alterar el PDF original
- El flujo separa responsabilidades entre USUARIO y SISTEMA:
    - USUARIO: decide, firma, endosa, confirma pago
    - SISTEMA: valida, registra, genera PDF, audita, calcula hash, protege inmutabilidad

## DIAGRAMA DE FLUJO (resumen)

USUARIO:                  SISTEMA:
---------------------------------------------
Registrarse / Login        Validar usuarios
Crear letra de cambio      Guardar BORRADOR
Seleccionar partes         Crear usuario PENDIENTE (si aplica)
Aceptar electrónicamente   Registrar FirmaElectronica
Generar PDF                Generar PDF inmutable
Firmar físicamente         Calcular hash + auditoría
Subir PDF firmado          Actualizar estado
Endosar letra              Registrar FirmaElectronica + generar anexo
Confirmar pago             Registrar FirmaElectronica + actualizar estado

## CONTACTO

Autor: Camilo Alejandro Soto Vega

Email: alejandro.vega.lims@gmail.com

Repositorio: https://github.com/CamiloSoto/letra-de-cambio.git
