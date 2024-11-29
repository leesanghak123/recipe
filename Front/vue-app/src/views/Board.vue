<template>
  <div class="board-container">
    <h2 class="board-title">자유게시판</h2>

    <div class="write-button-container">
      <button @click="goToWritePage" class="btn btn-write">글쓰기</button>
    </div>

    <table class="board-table">
      <thead>
        <tr>
          <th>순번</th>
          <th>제목</th>
          <th>글쓴이</th>
          <th>작성일</th>
          <th>댓글수</th>
          <th>조회수</th>
          <th>추천수</th>
        </tr>
      </thead>
      <tbody>
        <!-- boards의 배열 요소를 board라는 변수로 순회하며 board.id를 기준으로 tr 생성 -->
        <tr v-for="board in boards" :key="board.id">
          <td>{{ board.id }}</td>
          <td>{{ board.title.substring(0, 10) }}</td>
          <td>{{ board.username.substring(0, 8) }}</td>
          <td>{{ formatDate(board.createDate) }}</td>
          <td>{{ board.reply ? board.reply.length : 0 }}</td> <!-- 댓글수 -->
          <td>{{ board.count }}</td> <!-- 조회수 -->
          <td>{{ board.likes }}</td> <!-- 추천수 -->
        </tr>
      </tbody>
    </table>
    <ul class="pagination">
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <a class="page-link" @click.prevent="changePage(currentPage - 1)">&laquo;</a>
      </li>
      <li v-for="i in totalPages" :key="i" class="page-item" :class="{ active: currentPage === i }">
        <a class="page-link" @click.prevent="changePage(i)">{{ i }}</a>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === totalPages }">
        <a class="page-link" @click.prevent="changePage(currentPage + 1)">&raquo;</a>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      boards: [], // 게시판 데이터
      currentPage: 1, // 현재 페이지
      totalPages: 0, // 전체 페이지 수
    };
  },
  methods: {
    // 서버에서 게시판 데이터를 가져오는 함수
    async fetchBoards(page = 1) {
      try {
        // 로컬 스토리지에서 JWT 토큰을 가져옴
        const token = localStorage.getItem("jwt");

        // 토큰이 없으면 로그인이 필요하다는 메시지 출력
        if (!token) {
          console.error("JWT 토큰이 없습니다. 로그인이 필요합니다.");
          this.$router.push("/auth/loginForm"); // 로그인 페이지로 리다이렉트
          return;
        }

        // JWT 토큰을 포함하여 요청
        const response = await axios.get(`http://localhost:8002/api/boards`, {
          headers: {
            Authorization: `Bearer ${token}`, // 토큰 추가
          },
        });

        // 서버로부터 받은 데이터 처리
        this.boards = response.data.content; // 게시글 목록
        this.totalPages = response.data.totalPages; // 전체 페이지 수
        this.currentPage = page; // 현재 페이지 설정
      } catch (error) {
        console.error("게시글 목록을 가져오는 데 실패했습니다:", error);
      }
    },

    // 페이지를 변경하는 함수
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.fetchBoards(page);
      }
    },

    // 날짜를 형식화하는 함수
    formatDate(dateString) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(dateString).toLocaleDateString('ko-KR', options);
    },

    // 글쓰기 페이지로 이동
    goToWritePage() {
      this.$router.push('/board/write');
    },
  },
  mounted() {
    // 컴포넌트가 마운트되면 게시글 목록을 가져옴
    this.fetchBoards(this.currentPage);
  },
};
</script>

<style scoped>
/* 게시판 컨테이너 스타일 */
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 게시판 제목 스타일 */
.board-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

/* 게시판 테이블 스타일 */
.board-table {
  width: 100%;
  border-collapse: collapse;
}

.board-table th,
.board-table td {
  padding: 12px 15px;
  text-align: center;
}

.board-table th {
  background-color: #6c757d; /* 연한 회색 */
  color: white;
  font-weight: bold;
  border: none;
}

.board-table td {
  background-color: #fff;
  color: #555;
}

.board-table tr:nth-child(even) td {
  background-color: #f3f3f3;
}

.board-table tr:hover td {
  background-color: #e9ecef;
}

/* 페이지네이션 스타일 */
.pagination {
  display: flex;
  justify-content: center;
  padding-top: 20px;
  gap: 5px;
}

.page-item {
  list-style: none;
}

.page-link {
  display: block;
  padding: 8px 12px;
  margin: 0 3px;
  color: #6c757d; /* 연한 회색 */
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
}

.page-link:hover {
  background-color: #6c757d; /* 연한 회색 */
  color: #fff;
}

.page-item.disabled .page-link {
  color: #ccc;
  cursor: not-allowed;
  pointer-events: none;
}

.page-item.active .page-link {
  background-color: #6c757d; /* 연한 회색 */
  color: white;
  border-color: #6c757d;
}

/* 글쓰기 버튼 스타일 */
.write-button-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.btn-write {
  background-color: #f3f3f3; /* 테이블 tr과 동일한 색상 */
  color: #555; /* 글자 색상 */
  border: 1px solid #ddd; /* 테이블 경계선과 동일한 색상 */
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-size: 16px;
}

.btn-write:hover {
  background-color: #e9ecef; /* 호버시 색상 변경 */
}

.btn-write:focus {
  outline: none; /* 포커스 시 나타나는 기본 테두리 없애기 */
}
</style>