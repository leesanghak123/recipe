import { createRouter, createWebHistory } from 'vue-router' // url에 따라 서로 다른 컴포넌트를 렌더링 할 수 있다
import Board from '../views/Board.vue'
import Login from '../views/Login.vue'
import Join from '../views/Join.vue'
import axios from 'axios';
import BoardWrite from '../views/BoardWrite.vue'
import BoardDetail from '../views/BoardDetail.vue'
import BoardUpdate from '../views/BoardUpdate.vue'
import AiService from '../views/AiService.vue';
import Map from '../views/Map.vue'

// 페이지 경로 상수 (페이지 URL)
const LOGIN_PATH = '/auth/loginForm';
const JOIN_PATH = '/auth/joinForm';
const BOARD_PATH = '/';
const BOARD_WRITE_PATH = '/board/write'
const BOARD_DETAIL_PATH = '/board/detail/:id' // 동적 경로
const BOARD_UPDATE_PATH = '/board/update/:id'
const AI_SERVICE_PATH = '/ai/service'
const MAP_PATH = '/map'

// JWT 만료 여부 확인
function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1])); // JWT의 페이로드를 디코딩
    const expiration = payload.exp; // exp 시간 (초 단위)
    const now = Math.floor(new Date().getTime() / 1000); // 현재 시간 (밀리초 -> 초)
    return now > expiration; // 현재 시간이 만료 시간을 초과하는지 확인
  }catch (error) {
    console.error('Error decoding token:', error);
    return true; // 디코딩에 실패하면 토큰이 만료된 것으로 간주
  }
}

// 컴포넌트 렌더링 (path 호출 시 component 파일 읽음)
const routes = [
  {
    path: BOARD_PATH, // URL 경로
    name: 'board',    // 라우트 이름
    component: Board, // 컴포넌트
    meta: { requiresAuth: true }  // 인증이 필요
  },
  {
    path: BOARD_WRITE_PATH,
    name: 'boardWrite',
    component: BoardWrite,
    meta: { requiresAuth: true }
  },
  {
    path: BOARD_DETAIL_PATH,
    name: 'boardDetial',
    component: BoardDetail,
    meta: {requiresAuth: true }
  },
  {
    path: BOARD_UPDATE_PATH,
    name: 'boardUpdate',
    component: BoardUpdate,
    meta: { requiresAuth: true }
  },
  {
    path: LOGIN_PATH,
    name: 'login',
    component: Login
  },
  {
    path: JOIN_PATH,
    name: 'join',
    component: Join
  },
  {
    path: AI_SERVICE_PATH,
    name: 'AiService',
    component: AiService,
    meta: { requiresAuth: true }
  },
  {
    path: MAP_PATH,
    name: 'Map',
    component: Map,
    meta: { requiresAuth: true }
  }
]

// 페이지 전환
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // BASE_URL : 기본 URL 경로 설정, 현재 아무설정도 안되어 있기 때문에 "/"가 Base_url
  routes
})

// 라우터 전환 시 인증 여부 확인 (인증이 안되면 초기화)
router.beforeEach((to, from, next) => { // beforeEach : 라우터가 변경되기 전, to:이동하려는 라우트 객체, from:현재 라우트 객체
  const token = localStorage.getItem('jwt');

  if (to.matched.some(record => record.meta.requiresAuth)) { // requiresAuth 메타데이터가 있는 경우
    if (!token || isTokenExpired(token)) {
      localStorage.removeItem('jwt');
      delete axios.defaults.headers.common['Authorization']; // 헤더 초기화
      next(LOGIN_PATH); // 로그인 페이지
    } else {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // JWT 설정
      next();
    }
  } else {  // 인증이 필요하지 않은 경우
    next();
  }
});


export default router;