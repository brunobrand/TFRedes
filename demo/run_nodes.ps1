# verifique se os caminhos estão corretos para a maquina
$javaPath = "C:\Program Files\Java\jdk-17\bin\java.exe"
$projectPath = "C:\Users\Brão\Projetos\TFRedes\demo"

Write-Host "Encerrando processos anteriores..."
taskkill /f /im java.exe 2>$null

Write-Host "Iniciando os nós da rede..."

Write-Host "Iniciando Julia na porta 7000..."
$juliaCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_julia.txt 7001"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $juliaCommand

Start-Sleep -Seconds 2

Write-Host "Iniciando Brenda na porta 7001..."
$brendaCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_brenda.txt 7000"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $brendaCommand

Start-Sleep -Seconds 2

Write-Host "Iniciando Bruno na porta 7002..."
$brunoCommand = "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_bruno.txt 7002"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $brunoCommand

Write-Host "Todos os nós foram iniciados."


# Set-ExecutionPolicy RemoteSigned -Scope Process -Force
# .\run_nodes.ps1