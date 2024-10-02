<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
    <Button @click="getData">데이터 호출</Button>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import axios from 'axios'

export default {
  name: 'HomeView',
  methods: {
    getData() {
      axios
        .get('http://localhost:8002/')
        //.get('/api')
        .then((response) => {
          console.log(response)
        })
        .catch((error) => {
          console.log(error)
        })
      }    
    }
  }
  components: {
    HelloWorld
  }

</script>