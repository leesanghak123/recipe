<template>
  <div id="app">
    <nav class="navbar navbar-expand-md navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="/">Recipe</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#collapsibleNavbar"
          aria-controls="collapsibleNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="collapsibleNavbar">
          <ul class="navbar-nav ms-auto align-items-center">
            <li v-if="!isLoggedIn" class="nav-item">
              <router-link to="/auth/loginForm" class="nav-link">로그인</router-link>
            </li>
            <li v-if="!isLoggedIn" class="nav-item">
              <router-link to="/auth/joinForm" class="nav-link">회원가입</router-link>
            </li>
            <li v-else class="nav-item">
              <span class="nav-link">안녕하세요, {{ username }}님</span>
            </li>
            <li v-if="isLoggedIn" class="nav-item">
              <router-link to="/ai/service" class="nav-link">요리 AI</router-link>
            </li>
            <li v-if="isLoggedIn" class="nav-item">
              <router-link to="/" class="nav-link" @click="logout">로그아웃</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view />
    <footer class="footer mt-auto py-3 bg-light text-dark">
      <div class="container text-center">
        <span class="text-muted">Create by Sanghak | 📞010-0000-0000</span>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      isLoggedIn: !!localStorage.getItem("jwt"), // 로그인 상태 여부
      username: "", // 사용자 이름
    };
  },
  mounted() {
    this.decodeToken(); // 컴포넌트가 마운트되면 JWT를 디코딩
  },
  methods: {
    logout() {
      localStorage.removeItem("jwt");
      delete axios.defaults.headers.common["Authorization"];
      this.isLoggedIn = false;
      this.username = ""; // 로그아웃 시 사용자 이름 초기화
      this.$router.push("/auth/loginForm");
    },
    decodeToken() {
      const token = localStorage.getItem("jwt");
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split(".")[1])); // JWT의 payload 디코딩
          this.username = payload.username; // username 필드 추출
        } catch (error) {
          console.error("JWT 디코딩 실패:", error);
        }
      }
    },
  },
};
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.footer {
  font-size: 0.8em;
  color: #6c757d; /* 텍스트 색상 조정 */
}
</style>