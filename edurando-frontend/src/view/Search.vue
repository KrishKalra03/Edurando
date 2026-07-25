<script setup>
import { ref } from "vue"
import EdCard from "@/shared/ui/EdCard.vue"
import EdModal from "@/shared/ui/EdModal.vue"
import Footer from "@/modules/Footer/Footer.vue"
import { useUserStore } from "@/store/store"

const userStore = useUserStore()
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
</script>

<template>
  <section class="px-4 pt-28 pb-24 max-w-7xl mx-auto min-h-screen">
    <!-- Results found -->
    <template v-if="userStore.getSearchResult.length > 0">
      <div class="flex flex-col items-center mb-12">
        <h2 class="text-3xl font-bold mb-3" style="color: var(--text-primary)">
          Search Results
          <span class="text-lg font-normal ml-2" style="color: var(--text-muted)">({{ userStore.getSearchResult.length }})</span>
        </h2>
        <div class="h-1 w-16 rounded-full" style="background: var(--gradient-brand)"></div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 justify-items-center">
        <EdCard
          v-for="searchUser in userStore.getSearchResult"
          :key="searchUser.id"
          :item="searchUser"
          @show-modal="setShowModal"
        />
      </div>
    </template>

    <!-- No results -->
    <div v-else class="flex flex-col items-center justify-center h-[60vh]">
      <div class="w-16 h-16 rounded-2xl flex items-center justify-center mb-5"
           style="background: var(--bg-elevated); border: 1px solid var(--border-default)">
        <font-awesome-icon icon="fa-solid fa-magnifying-glass" class="text-2xl" style="color: var(--text-muted)" />
      </div>
      <h2 class="text-2xl font-bold mb-2" style="color: var(--text-primary)">No Matches Found</h2>
      <p class="text-sm" style="color: var(--text-muted)">Try a different search term</p>
    </div>
  </section>

  <Footer />

  <EdModal v-if="showModal" :item="selectedUser" @close-modal="closeModal" />
</template>
