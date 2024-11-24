import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css'; // 부트스트랩 CSS
import 'bootstrap/dist/js/bootstrap.bundle.min.js'; // 부트스트랩 JavaScript
import axios from 'axios'; // Axios 임포트

// JWT 토큰을 기본 헤더에 설정
const token = localStorage.getItem('jwt');
if (token) { // 토큰이 있다면 Axios 헤더에 JWT 토큰을 포함
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Vue 애플리케이션 생성 및 설정
createApp(App)
    .use(router)
    .mount('#app');  // 애플리케이션의 진입점(현재 App.vue의 id가 app으로 설정되어 있다)