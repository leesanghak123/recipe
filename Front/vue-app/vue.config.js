const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

/* vue 파일과 연동이 안되네 (vue 에서 axios 통신 설정)
const target = 'http://localhost:8002'
module.exports - {
  devServer: {
    port: 8002,
    proxy: {
      '/api': {
        target,
        changOrigin: true
      }
    }
  }
}
*/