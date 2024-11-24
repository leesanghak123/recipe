<template>
  <div class="board-container">
    <h2 class="board-title">자유게시판</h2>
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
      boards: [],
      currentPage: 1,
      totalPages: 0
    };
  },
  methods: {
    async fetchBoards(page = 1) {
      try {
        const response = await axios.get(`http://localhost:8002/api/boards`);
        this.boards = response.data.content;
        this.totalPages = response.data.totalPages;
        this.currentPage = page;
      } catch (error) {
        console.error("Error fetching boards:", error);
      }
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.fetchBoards(page);
      }
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(dateString).toLocaleDateString('ko-KR', options);
    }
  },
  mounted() {
    this.fetchBoards(this.currentPage);
  }
};
</script>

<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.board-title {
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

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
</style>