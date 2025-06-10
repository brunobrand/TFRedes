# Verifique se estes caminhos estão corretos para a sua máquina
$javaPath = "C:\Program Files\Java\jdk-17\bin\java.exe"
$projectPath = "C:\Users\Brão\Projetos\TFRedes\demo"

# Encerra processos Java anteriores para garantir um início limpo
Write-Host "Encerrando processos Java anteriores..."
taskkill /f /im java.exe 2>$null

Write-Host "Iniciando os nós da rede..."

# Inicia Julia (Nó Gerador) escutando na porta 7000
Write-Host "Iniciando Julia na porta 7000..."
# MODIFICADO: Adicionada a porta de escuta 7000 como argumento
$juliaCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_julia.txt 7001"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $juliaCommand

Start-Sleep -Seconds 2

# Inicia Brenda escutando na porta 7001
Write-Host "Iniciando Brenda na porta 7001..."
# MODIFICADO: Adicionada a porta de escuta 7001 como argumento
$brendaCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_brenda.txt 7000"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $brendaCommand

Start-Sleep -Seconds 2

# Inicia Bruno escutando na porta 7002
Write-Host "Iniciando Bruno na porta 7002..."
# MODIFICADO: Adicionada a porta de escuta 7002 como argumento
$brunoCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_bruno.txt 7002"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $brunoCommand

Write-Host "Todos os nós foram iniciados."

# Para executar este script no PowerShell, você pode precisar ajustar a política de execução.
# Descomente e execute a linha abaixo no seu terminal PowerShell se encontrar erros de permissão:
# Set-ExecutionPolicy RemoteSigned -Scope Process -Force
#
# Depois, execute o script com:
# .\run_nodes.ps1