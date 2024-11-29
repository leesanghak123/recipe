import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // 라우터
import $ from 'jquery'; // jQuery 임포트
import axios from 'axios'; // HTTP 요청

// JWT 토큰을 기본 헤더에 설정
// 새로고침(F5)를 한 경우 JWT를 다시 header에 설정
const token = localStorage.getItem('jwt');
if (token) { // 토큰이 있다면 Axios 헤더에 JWT 토큰을 포함
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// jQuery를 전역으로 등록
// jQuery 로드 후에 summernote가 초기화 되어야하기 때문에 시작지점에서 설정
window.$ = window.jQuery = $;

// Vue 애플리케이션 생성 및 설정
createApp(App)
    .use(router)
    .mount('#app');  // 애플리케이션의 진입점(현재 App.vue의 id가 app으로 설정되어 있다)