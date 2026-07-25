<script setup>
import { ref, onMounted } from "vue"
import axios from "axios"
import EdCard from "@/shared/ui/EdCard.vue"
import EdModal from "@/shared/ui/EdModal.vue"
import Footer from "@/modules/Footer/Footer.vue"
import { useUserStore } from "@/store/store"

const userStore = useUserStore()
const responseData = ref([])
const loading = ref(true)
const showModal = ref(false)
const selectedUser = ref(null)

function setShowModal(data) {
  selectedUser.value = data
  showModal.value = true
}

function closeModal() {
  selectedUser.value = null
  showModal.value = false
}

onMounted(async () => {
  try {
    const response = await axios.get("/top-users")
    responseData.value = response.data
  } catch (error) {
    console.log(error)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <!-- Hero Section -->
  <section class="relative overflow-hidden pt-32 pb-16 px-4">
    <div class="orb orb-left"></div>
    <div class="orb orb-right"></div>

    <div class="max-w-3xl mx-auto text-center relative">
      <div
        class="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full text-xs font-semibold mb-6 border"
        style="background: var(--bg-elevated); border-color: var(--border-default); color: var(--text-secondary)"
      >
        <span class="w-2 h-2 rounded-full bg-green-400 animate-pulse"></span>
        Top-rated educators — updated daily
      </div>

      <h1 class="text-5xl md:text-6xl font-extrabold mb-5 leading-tight" style="color: var(--text-primary)">
        Find Your Perfect
        <span class="gradient-text"> Teacher</span>
      </h1>

      <p class="text-lg mb-8 max-w-xl mx-auto" style="color: var(--text-muted)">
        Connect with the best educators, learn new skills, and grow faster than ever before.
      </p>

      <div v-if="userStore.isLoggedOut" class="flex items-center justify-center gap-3 flex-wrap">
        <RouterLink to="/register">
          <button
            class="px-6 py-3 rounded-xl text-sm font-semibold text-white transition-all duration-200 hover:scale-105"
            style="background: var(--gradient-brand)"
          >
            Get Started Free
          </button>
        </RouterLink>
        <RouterLink to="/login">
          <button
            class="px-6 py-3 rounded-xl text-sm font-semibold border transition-all duration-200 hover:scale-105"
            style="color: var(--text-secondary); border-color: var(--border-default); background: transparent"
          >
            Sign In
          </button>
        </RouterLink>
      </div>
    </div>
  </section>

  <!-- Quick stats -->
  <section class="px-4 mb-14">
    <div class="max-w-xl mx-auto grid grid-cols-3 gap-4">
      <div class="stat-card">
        <p class="text-2xl font-bold gradient-text">10+</p>
        <p class="stat-label">Top Teachers</p>
      </div>
      <div class="stat-card">
        <p class="text-2xl font-bold gradient-text">100%</p>
        <p class="stat-label">Verified</p>
      </div>
      <div class="stat-card">
        <p class="text-2xl font-bold gradient-text">Free</p>
        <p class="stat-label">To Start</p>
      </div>
    </div>
  </section>

  <!-- Top Teachers Grid -->
  <section class="px-4 pb-24 max-w-7xl mx-auto">
    <div class="flex flex-col items-center mb-12">
      <h2 class="text-3xl font-bold mb-3" style="color: var(--text-primary)">Our Top 10 Teachers</h2>
      <div class="h-1 w-16 rounded-full" style="background: var(--gradient-brand)"></div>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 justify-items-center">
      <EdCard
        v-for="topUser in responseData"
        :key="topUser.id"
        :item="topUser"
        @show-modal="setShowModal"
      />
    </div>

    <div v-if="loading" class="text-center py-20">
      <p class="text-base" style="color: var(--text-muted)">Loading teachers…</p>
    </div>
    <div v-else-if="responseData.length === 0" class="text-center py-20">
      <p class="text-base" style="color: var(--text-muted)">No teachers found.</p>
    </div>
  </section>

  <Footer />

  <EdModal v-if="showModal" :item="selectedUser" @close-modal="closeModal" />
</template>

<style scoped>
.gradient-text {
  background: var(--gradient-brand);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-card {
  text-align: center;
  padding: 16px;
  border-radius: 16px;
  border: 1px solid var(--border-subtle);
  background: var(--bg-surface);
}

.stat-label {
  font-size: 11px;
  margin-top: 2px;
  color: var(--text-muted);
}

.orb {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(80px);
  opacity: 0.16;
}
.orb-left {
  width: 480px;
  height: 480px;
  top: -80px;
  left: -80px;
  background: radial-gradient(circle, #6366f1, transparent);
}
.orb-right {
  width: 380px;
  height: 380px;
  top: 20px;
  right: -60px;
  background: radial-gradient(circle, #a855f7, transparent);
}
</style>
