<template>
    <div class="container mt-5">
      <div class="card shadow-sm">
        <div class="card-body">
          <form @submit.prevent="fetchRecipes"> <!-- 제출 시 새로고침을 막고 fetchRecipes 메서드 실행 -->
            <!-- 요리 방법 셀렉트 박스(드롭 다운) -->
            <div class="form-group mb-4">
              <!-- cookingMethod라는 이름으로 아래 select와 연결 -->
              <!-- form-select: 드롭 다운 형식, 선택된 값은 v-model에 저장 -->
              <!-- label + select + option의 조합 --> 
              <label for="cookingMethod" class="form-label fw-bold">요리 방법</label>
              <select
                id="cookingMethod"
                class="form-select"
                v-model="selectedMethod"
              > <!-- select의 끝임을 브라우저에게 알려줌 -->
                <!-- value="": 기본값이 아무것도 선택되지 않은 상태 -->
                <option value="">-- 요리 방법 선택 --</option>
                <!-- key는 vue에서의 고유 식별자, value는 실제 값 -->
                <option
                  v-for="method in cookingMethods"
                  :key="method"
                  :value="method"
                >
                  <!-- 화면에 표시되는 값-->
                  {{ method }}
                </option>
              </select>
            </div>
  
            <!-- 요리 방식 셀렉트 박스 -->
            <div class="form-group mb-4">
              <label for="cookingCategory" class="form-label fw-bold">요리 방식</label>
              <select
                id="cookingCategory"
                class="form-select"
                v-model="selectedCategory"
              >
                <option value="">-- 요리 카테고리 선택 --</option>
                <option
                  v-for="category in cookingCategories"
                  :key="category"
                  :value="category"
                >
                  {{ category }}
                </option>
              </select>
            </div>
  
            <!-- 재료 입력 필드 -->
            <div class="form-group mb-4">
              <label for="ingredients" class="form-label fw-bold">재료</label>
              <input
                id="ingredients"
                type="text"
                class="form-control"
                placeholder="재료를 입력하세요 (예: 양파, 당근, 닭고기)"
                v-model="enteredIngredients"
              />
            </div>
  
            <!-- 검색 버튼 -->
            <div class="text-end mt-4">
              <button class="btn btn-success btn-sm px-3" type="submit">검색</button>
            </div>
          </form>
  
          <!-- 결과 출력 -->
          <!-- 결과가 있다면 -->
          <div v-if="recipes.length > 0" class="mt-4">
            <h5>검색 결과:</h5>
            <ul>
              <!-- 결과를 결과 개수 만큼 출력 -->
              <li v-for="recipe in recipes" :key="recipe.content">
                <strong>방법: {{ recipe.cookingMethod }}</strong> | 
                <strong>방식: {{ recipe.cookingCategory }}</strong> |
                <strong>재료: {{ recipe.ingredients }}</strong>
                <!-- 줄바꿈 글자깨짐 방지 적용 -->
                <p style="white-space: pre-line;">{{ decodeContent(recipe.content) }}</p>
              </li>
            </ul>
          </div>
          <div v-else-if="searched" class="mt-4 text-muted">
            검색 결과가 없습니다.
          </div>
        </div>
      </div>
    </div>
  </template>  
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return { // 초기 상태 설정
        cookingMethods: [
          "굽기", "기타", "끓이기", "데치기", "무침", "볶음", "부침", "비빔",
          "삶기", "절임", "조림", "튀김", "회", "찜", "상관없음"
        ],
        cookingCategories: [
          "간식", "기타", "다이어트", "도시락", "명절", "손님접대", "술안주",
          "야식", "영양식", "이유식", "일상", "초스피드", "해장", "상관없음"
        ],
        selectedMethod: "",       // 선택된 요리 방법
        selectedCategory: "",     // 선택된 요리 방식
        enteredIngrediens: "",   // 입력된 재료
        recipes: [],              // 결과 데이터를 저장
        searched: false,          // 검색 여부 확인
      };
    },
    methods: {
      // 줄바꿈 글자깨짐 방지
      decodeContent(content) {
        return content.replace(/\\n/g, '\n').replace(/\\"/g, '"');
      },  

      // form 제출 부분
      async fetchRecipes() {
        if (!this.selectedMethod || !this.selectedCategory || !this.enteredIngredients) {
          alert("요리 방법, 요리 카테고리, 재료를 모두 입력해주세요.");
          return;
        }
  
        try {
          const response = await axios.post("http://localhost:8002/api/ai/service", {
            cookingMethod: this.selectedMethod, // 입력한 값을 server에게 보냄
            cookingCategory: this.selectedCategory,
            ingredients: this.enteredIngredients,
          });
          console.log(response.data); // 응답 데이터 확인
          this.recipes = [response.data]; // 객체 형태로 반환되면 배열로 감싸서 처리
          this.searched = true; // 검색 시도 표시
        } catch (error) {
          console.error("검색 실패:", error);
          this.searched = true;
          this.recipes = [];
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    max-width: 800px;
  }
  
  .card {
    border-radius: 10px;
  }
  
  .form-select {
    border-radius: 8px;
    border: 1px solid #ced4da;
  }
  t
  .form-select:focus {
    border-color: #28a745;
    box-shadow: 0 0 5px rgba(40, 167, 69, 0.5);
  }
  
  .btn-success {
    background-color: #28a745;
    color: white;
  }
  
  .btn-success:hover {
    background-color: #218838;
  }
  </style>  