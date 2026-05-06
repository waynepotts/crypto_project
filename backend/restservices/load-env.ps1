Get-Content .env.dev | ForEach-Object {

    if ($_ -match "^\s*#") {
        return
    }

    if ($_ -match "^\s*$") {
        return
    }

    $name, $value = $_ -split '=', 2

    [System.Environment]::SetEnvironmentVariable(
        $name,
        $value,
        "Process"
    )

    Write-Host "Loaded $name"
}

