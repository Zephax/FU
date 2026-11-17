$configs = Get-NetIPConfiguration
foreach ($config in $configs) {
    if ($null -ne $config.IPv4DefaultGateway -and $config.NetAdapter.Status -eq 'Up' -and $null -ne $config.IPv4Address) {
        $config.IPv4Address.IPAddress
    }
}