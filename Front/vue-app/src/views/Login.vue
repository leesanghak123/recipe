<template>
  <div class="d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4" style="width: 400px;">
      <h3 class="card-title text-center mb-4">로그인</h3>
      <form @submit.prevent="login"> <!-- 양식 제출 시 login 메서드 실행, prevent: 새로고침 방지 -->
        <div class="form-group">
          <input type="text" v-model="username" class="form-control" placeholder="아이디" id="username" required />
        </div>
        <div class="form-group">
          <input type="password" v-model="password" class="form-control" placeholder="비밀번호" id="password" required />
        </div>
        <div class="text-end"> <!-- 오른쪽 정렬 -->
          <button type="submit" class="btn btn-primary w-100">로그인</button>
        </div>
        <p v-if="errorMessage" class="text-danger mt-2">{{ errorMessage }}</p>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  // 사용자의 입력 값 저장
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
    };
  },
  methods: {
    login() {
      axios.post('http://localhost:8002/login', {
        username: this.username,
        password: this.password
      })
      .then(response => { // 서버 응답
        const token = response.headers['authorization'];
        if (token) {
          const jwt = token.split(' ')[1];
          localStorage.setItem('jwt', jwt);
          axios.defaults.headers.common['Authorization'] = `Bearer ${jwt}`;
          this.$router.push('/');
          this.$root.isLoggedIn = true;
        } else {
          this.errorMessage = '로그인 중 오류가 발생했습니다.';
        }
      })
      .catch(error => {
        this.errorMessage = '아이디 또는 비밀번호가 잘못되었습니다.';
      });
    },
  },
};
</script>

<style scoped>
.form-group {
  margin-bottom: 20px;
}
</style>