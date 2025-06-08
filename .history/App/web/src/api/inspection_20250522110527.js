export async function getInspectionPoint(code) {
  // mock数据，实际开发请替换为真实接口
  return Promise.resolve({
    data: {
      data: {
        code,
        id: 1,
        name: '测试巡检点'
      }
    }
  });
} 