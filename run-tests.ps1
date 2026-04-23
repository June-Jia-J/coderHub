# CoderHub 自动化测试启动脚本
# PowerShell 5 兼容版本

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    CoderHub 闭环测试自动化脚本" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查Docker状态
Write-Host "🔍 检查 Docker 状态..." -ForegroundColor Yellow
$dockerRunning = $true
try {
    docker ps 2>&1 | Out-Null
    Write-Host "   ✅ Docker 正在运行" -ForegroundColor Green
} catch {
    Write-Host "   ⚠️  Docker 未运行，请先启动 Docker Desktop" -ForegroundColor Yellow
    $dockerRunning = $false
}
Write-Host ""

if ($dockerRunning) {
    Write-Host "🐳 启动 Docker Compose 服务..." -ForegroundColor Yellow
    Write-Host "   这可能需要几分钟..." -ForegroundColor Gray
    docker compose up --build -d
    
    Write-Host ""
    Write-Host "⏳ 等待服务启动..." -ForegroundColor Yellow
    Write-Host "   后端: localhost:8207" -ForegroundColor Gray
    Write-Host "   前端: localhost:3530" -ForegroundColor Gray
    Write-Host ""
    Start-Sleep 20
}

# 检查后端是否可用
Write-Host "🔍 检查后端服务..." -ForegroundColor Yellow
$backendReady = $false
$maxRetries = 30
$retryCount = 0

while (-not $backendReady -and $retryCount -lt $maxRetries) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8207/health" -TimeoutSec 2 -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            $backendReady = $true
            Write-Host "   ✅ 后端服务已就绪 (端口 8207)" -ForegroundColor Green
        }
    } catch {
        $retryCount++
        Write-Host "   等待中... ($retryCount/$maxRetries)" -ForegroundColor Gray
        Start-Sleep 2
    }
}

Write-Host ""

if ($backendReady) {
    Write-Host "🧪 运行 API 自动化测试..." -ForegroundColor Yellow
    Write-Host ""
    
    node test-api.js
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "📋 下一步手动验证请参考：" -ForegroundColor Cyan
    Write-Host "   test-frontend-checklist.md" -ForegroundColor White
    Write-Host ""
    Write-Host "🌐 服务访问地址：" -ForegroundColor Cyan
    Write-Host "   前端页面: http://localhost:3530" -ForegroundColor White
    Write-Host "   后端API:  http://localhost:8207" -ForegroundColor White
    Write-Host ""
    Write-Host "👤 测试账号: admin / admin123" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
} else {
    Write-Host "❌ 后端服务启动超时" -ForegroundColor Red
    Write-Host "💡 请检查：" -ForegroundColor Yellow
    Write-Host "   1. Docker Desktop 是否已启动" -ForegroundColor White
    Write-Host "   2. 端口 8207 是否被占用" -ForegroundColor White
    Write-Host "   3. 手动执行: docker compose logs backend" -ForegroundColor White
    exit 1
}
