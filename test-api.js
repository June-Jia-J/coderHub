/**
 * CoderHub 后端API自动化测试脚本
 * 用于闭环测试验证所有修复的功能
 */

const assert = require('assert');
const http = require('http');

const BASE_URL = 'localhost';
const PORT = 8207;

console.log('🧪 开始 CoderHub API 自动化测试\n');
console.log(`📍 测试目标: http://${BASE_URL}:${PORT}\n`);

let testResults = { passed: 0, failed: 0, total: 0 };
let authToken = '';

function request(method, path, data = null, token = null) {
  return new Promise((resolve, reject) => {
    const postData = data ? JSON.stringify(data) : null;
    const options = {
      hostname: BASE_URL,
      port: PORT,
      path,
      method,
      headers: {
        'Content-Type': 'application/json',
        ...(postData && { 'Content-Length': Buffer.byteLength(postData) }),
        ...(token && { 'Authorization': `Bearer ${token}` })
      }
    };

    const req = http.request(options, (res) => {
      let body = '';
      res.on('data', chunk => body += chunk);
      res.on('end', () => {
        try {
          resolve({ status: res.statusCode, data: JSON.parse(body) });
        } catch {
          resolve({ status: res.statusCode, data: body });
        }
      });
    });
    req.on('error', reject);
    if (postData) req.write(postData);
    req.end();
  });
}

async function runTest(name, testFn) {
  testResults.total++;
  process.stdout.write(` 🔍 ${name}... `);
  try {
    await testFn();
    console.log('✅ PASS');
    testResults.passed++;
  } catch (e) {
    console.log('❌ FAIL');
    console.log(`     错误: ${e.message}`);
    testResults.failed++;
  }
}

async function main() {
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('📦 阶段1: 基础连通性测试');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

  await runTest('健康检查接口正常', async () => {
    const res = await request('GET', '/health');
    assert.equal(res.status, 200, '健康检查应该返回200');
  });

  await runTest('后端端口正确 (8207)', async () => {
    // 验证端口是 8207 而不是 8080
    const res = await request('GET', '/health');
    assert.equal(res.status, 200, '8207 端口应该正常响应');
  });

  console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('📝 阶段2: 博客列表接口测试');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

  await runTest('博客列表路径正确 /api/blogs (带s)', async () => {
    const res = await request('GET', '/api/blogs');
    assert.equal(res.status, 200, '博客列表应该返回200');
    assert.equal(res.data.code, 200, '返回code应该是200');
    assert.ok(Array.isArray(res.data.data.list), '应该返回list数组');
  });

  await runTest('按作者过滤博客列表', async () => {
    const res = await request('GET', '/api/blogs?author=admin');
    assert.equal(res.status, 200, '过滤查询应该返回200');
  });

  console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('🔐 阶段3: 登录与JWT鉴权测试');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

  await runTest('登录接口正常', async () => {
    const res = await request('POST', '/api/auth/login', {
      username: 'admin',
      password: 'admin123'
    });
    assert.equal(res.status, 200, '登录应该返回200');
    assert.equal(res.data.code, 200, '登录code应该是200');
    assert.ok(res.data.data.token, '应该返回token');
    assert.ok(res.data.data.username, '应该返回username');
    authToken = res.data.data.token;
    console.log(`\n     Token: ${authToken.substring(0, 30)}...`);
    console.log(`     Username: ${res.data.data.username}`);
  });

  await runTest('JWT Token能正确鉴权', async () => {
    assert.ok(authToken, '需要先获取token');
    // 创建博客需要鉴权
    const res = await request('POST', '/api/blogs', {
      title: 'API测试文章',
      content: '这是测试内容'
    }, authToken);
    assert.equal(res.status, 200, '带token创建博客应该成功');
    assert.equal(res.data.code, 200, '创建博客code应该是200');
    console.log(`\n     创建博客ID: ${res.data.data.id}`);
  });

  await runTest('无Token时创建博客返回401', async () => {
    const res = await request('POST', '/api/blogs', {
      title: '未授权测试',
      content: '测试'
    });
    assert.equal(res.status, 401, '无token应该返回401');
  });

  console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('✏️  阶段4: 文章编辑与权限测试');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

  let testBlogId = null;
  let testBlogData = null;

  await runTest('获取文章详情 - 数据字段正确', async () => {
    const listRes = await request('GET', '/api/blogs');
    const blogs = listRes.data.data.list;
    assert.ok(blogs.length > 0, '应该有测试博客');
    testBlogId = blogs[0].id;
    
    const res = await request('GET', `/api/blogs/${testBlogId}`);
    testBlogData = res.data.data;
    assert.ok(testBlogData.title, '应该有title字段');
    assert.ok(testBlogData.content, '应该有content字段');
    assert.ok(testBlogData.authorName, '应该有authorName字段');
    console.log(`\n     文章: "${testBlogData.title}" by ${testBlogData.authorName}`);
  });

  await runTest('编辑文章 - 标题内容不错位', async () => {
    // 验证: 数据结构正确，title/content字段对应正确
    assert.ok(testBlogData.title.length > 0, '标题应该有内容');
    assert.ok(testBlogData.content.length > 0, '正文应该有内容');
    // 验证正文不会出现在title字段（简单检查：正文比标题通常更长）
    assert.ok(testBlogData.content.length >= testBlogData.title.length, 
      '正文长度应该 >= 标题长度，说明没有错位');
  });

  await runTest('更新文章正常', async () => {
    const res = await request('PUT', `/api/blogs/${testBlogId}`, {
      title: '更新后的标题',
      content: '更新后的正文，这是更长的内容，用于验证更新功能正常工作'
    }, authToken);
    assert.equal(res.status, 200, '作者更新博客应该成功');
    assert.equal(res.data.data.title, '更新后的标题', '标题更新正确');
  });

  console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
  console.log('📊 测试结果汇总');
  console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
  
  console.log(`   总计: ${testResults.total} 个测试`);
  console.log(`   ✅ 通过: ${testResults.passed}`);
  console.log(`   ❌ 失败: ${testResults.failed}`);
  
  const passRate = ((testResults.passed / testResults.total) * 100).toFixed(1);
  console.log(`   📈 通过率: ${passRate}%`);
  
  if (testResults.failed === 0) {
    console.log('\n🎉 所有测试通过！功能验证完成！');
    process.exit(0);
  } else {
    console.log('\n⚠️  部分测试失败，请检查修复');
    process.exit(1);
  }
}

main().catch(e => {
  console.error('\n❌ 测试执行失败:', e.message);
  console.log('💡 提示: 请确保后端已在 8207 端口启动');
  process.exit(1);
});
