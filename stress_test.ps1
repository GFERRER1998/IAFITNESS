$url = "http://localhost:8080/api/activities"
Write-Host "Starting Stress Test: 100 concurrent requests with unique User IDs..."
$startTime = Get-Date

# Simulating 100 concurrent users using Parallel processing
$results = 1..100 | ForEach-Object -Parallel {
    $userId = "user-{0:D3}" -f $_
    $requestStart = Get-Date
    try {
        # Using curl.exe for better reliability in parallel execution
        $output = curl.exe -s -o /dev/null -w "%{http_code}" -H "X-User-ID: $userId" http://localhost:8080/api/activities
        $requestEnd = Get-Date
        if ($output -eq "200") {
            return [PSCustomObject]@{
                UserId  = $userId
                Status  = "Success"
                Latency = ($requestEnd - $requestStart).TotalMilliseconds
            }
        } else {
            return [PSCustomObject]@{
                UserId  = $userId
                Status  = "Error: HTTP $output"
                Latency = 0
            }
        }
    } catch {
        return [PSCustomObject]@{
            UserId  = $userId
            Status  = "Error: $($_.Exception.Message)"
            Latency = 0
        }
    }
} -ThrottleLimit 100

$endTime = Get-Date
$totalDuration = ($endTime - $startTime).TotalSeconds

$successCount = ($results | Where-Object { $_.Status -eq "Success" }).Count
$errorCount = 100 - $successCount
$avgLatency = ($results | Where-Object { $_.Status -eq "Success" } | Measure-Object Latency -Average).Average
$maxLatency = ($results | Where-Object { $_.Status -eq "Success" } | Measure-Object Latency -Maximum).Maximum

Write-Host "`n--- STRESS TEST REPORT ---" -ForegroundColor Cyan
Write-Host "Total Requests: 100"
Write-Host "Success: $successCount" -ForegroundColor Green
$errorColor = "Gray"
if ($errorCount -gt 0) { $errorColor = "Red" }
Write-Host "Errors:  $errorCount" -ForegroundColor $errorColor
Write-Host "Total Time: $($totalDuration.ToString('F2')) seconds"
Write-Host "Avg Latency: $($avgLatency.ToString('F2')) ms"
Write-Host "Max Latency: $($maxLatency.ToString('F2')) ms"
Write-Host "--------------------------"
