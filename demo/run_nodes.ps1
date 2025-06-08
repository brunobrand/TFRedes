$javaPath = "C:\Program Files\Java\jdk-17\bin\java.exe"
$projectPath = "C:\Users\Brão\Projetos\TFRedes\demo"

# Encerra processos anteriores
taskkill /f /im java.exe 2>$null

# Inicia Julia primeiro
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_julia.txt"

Start-Sleep -Seconds 2

# Inicia Brenda
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_brenda.txt"

Start-Sleep -Seconds 2

# Inicia Bruno por último
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$projectPath'; & '$javaPath' -cp target/classes com.tfredes.Main config_bruno.txt"

# Set-ExecutionPolicy RemoteSigned -Scope Process -Force
#.\run_nodes.ps1