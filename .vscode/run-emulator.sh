#!/usr/bin/env bash
# Script de apoio para o .vscode/launch.json do Sentinela.
# Sobe o emulador Android (se necessário), builda, instala e abre o app.
#
# Uso:
#   run-emulator.sh                  -> emulador + build + install + abrir
#   run-emulator.sh --only-emulator  -> só sobe o emulador
#   run-emulator.sh --skip-emulator  -> assume emulador/dispositivo já conectado

set -euo pipefail

# SDK: ANDROID_HOME/ANDROID_SDK_ROOT, senão o caminho padrão do Windows
# (%LOCALAPPDATA%\Android\Sdk) ou do Linux/macOS (~/Android/Sdk).
if [ -n "${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}" ]; then
  SDK="${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
elif [ -n "${LOCALAPPDATA:-}" ] && [ -d "$LOCALAPPDATA/Android/Sdk" ]; then
  SDK="$LOCALAPPDATA/Android/Sdk"
else
  SDK="$HOME/Android/Sdk"
fi
export ANDROID_HOME="$SDK"
ADB="$SDK/platform-tools/adb"
EMULATOR="$SDK/emulator/emulator"

# AVD: use AVD_NAME=... para escolher; por padrão pega o primeiro da máquina.
AVD_NAME="${AVD_NAME:-$("$EMULATOR" -list-avds 2>/dev/null | head -n 1 | tr -d '')}"

# O Gradle precisa de Java 17+; se o JAVA_HOME não servir, usa o JDK que vem
# com o Android Studio (evita baixar outro Java).
JBR_STUDIO="/c/Program Files/Android/Android Studio/jbr"
if [ -d "$JBR_STUDIO" ] && ! "${JAVA_HOME:+$JAVA_HOME/bin/}java" -version 2>&1 | grep -qE 'version "(1[7-9]|2[0-9])'; then
  export JAVA_HOME="$JBR_STUDIO"
fi
APP_ID="com.sentinela.app"
ACTIVITY=".MainActivity"

SKIP_EMULATOR=false
ONLY_EMULATOR=false
for arg in "$@"; do
  case "$arg" in
    --skip-emulator) SKIP_EMULATOR=true ;;
    --only-emulator) ONLY_EMULATOR=true ;;
  esac
done

if [ ! -x "$ADB" ]; then
  echo "adb não encontrado em $ADB. Ajuste ANDROID_HOME ou o caminho do SDK no script." >&2
  exit 1
fi

start_emulator_if_needed() {
  if "$ADB" devices | grep -q "^emulator-"; then
    echo "Emulador já em execução, pulando o start."
    return
  fi

  if ! "$EMULATOR" -list-avds | grep -qx "$AVD_NAME"; then
    echo "AVD '$AVD_NAME' não encontrado. AVDs disponíveis:" >&2
    "$EMULATOR" -list-avds >&2
    exit 1
  fi

  echo "Iniciando o emulador ($AVD_NAME)..."
  nohup "$EMULATOR" -avd "$AVD_NAME" > /tmp/sentinela-emulator.log 2>&1 &
  disown

  echo "Aguardando o dispositivo aparecer..."
  "$ADB" wait-for-device

  echo "Aguardando o boot completar..."
  until [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; do
    sleep 2
  done
  echo "Emulador pronto."
}

if [ "$SKIP_EMULATOR" = false ]; then
  start_emulator_if_needed
fi

if [ "$ONLY_EMULATOR" = true ]; then
  exit 0
fi

cd "$(dirname "$0")/.."

echo "Compilando e instalando o app (./gradlew installDebug)..."
./gradlew installDebug

echo "Abrindo o app no emulador..."
# -W espera a Activity abrir (logo após o boot o launcher às vezes "rouba" o foco).
"$ADB" shell am start -W -n "$APP_ID/$ACTIVITY"
