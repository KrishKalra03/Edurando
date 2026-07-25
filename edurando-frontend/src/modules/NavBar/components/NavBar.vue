<template>
  <header class="relative z-50">
    <!-- Dropdown menu -->
    <Transition name="dropdown">
      <div
        v-if="showSubmenu"
        class="fixed top-[74px] right-4 w-52 rounded-2xl border overflow-hidden z-50"
        style="background: var(--bg-surface); border-color: var(--border-default); box-shadow: var(--shadow-elevated)"
        @mouseleave="showSubmenu = false"
      >
        <div class="p-2 space-y-0.5">
          <RouterLink to="/editProfile" class="dropdown-item" @click="showSubmenu = false">
            <font-awesome-icon icon="fa-solid fa-user" class="w-4 opacity-60" />
            Profile
          </RouterLink>
          <RouterLink to="/chat" class="dropdown-item" @click="showSubmenu = false">
            <font-awesome-icon icon="fa-solid fa-paper-plane" class="w-4 opacity-60" />
            Chat
          </RouterLink>
          <div class="h-px my-1" style="background: var(--border-subtle)"></div>
          <button class="dropdown-item w-full logout-btn" @click="logOut">
            <font-awesome-icon icon="fa-solid fa-right-to-bracket" class="w-4 opacity-60" />
            Logout
          </button>
        </div>
      </div>
    </Transition>

    <!-- Main Navbar -->
    <nav
      class="fixed top-0 left-0 right-0 z-40 h-[68px] flex items-center justify-between px-5 md:px-8 backdrop-blur-xl border-b"
      style="background: var(--nav-bg); border-color: var(--nav-border)"
    >
      <!-- Logo -->
      <button class="flex items-center gap-2.5 flex-shrink-0" @click="redirectToHome">
        <img class="w-8 h-8 rounded-xl object-cover" src="@/assets/logo/logo_image.png" alt="Logo" />
        <span class="text-xl font-bold gradient-text">Edurando</span>
      </button>

      <!-- Search bar -->
      <div class="hidden md:block flex-1 max-w-xs mx-8">
        <div class="relative">
          <font-awesome-icon
            icon="fa-solid fa-magnifying-glass"
            class="absolute left-3.5 top-1/2 -translate-y-1/2 text-xs pointer-events-none"
            style="color: var(--text-muted)"
          />
          <input
            v-model="searchTerm"
            type="text"
            placeholder="Search teachers…"
            class="search-input w-full pl-9 pr-4 py-2.5 rounded-xl text-sm border outline-none transition-all duration-200"
            style="background: var(--bg-elevated); border-color: var(--border-subtle); color: var(--text-primary)"
          />
        </div>
      </div>

      <!-- Right-side actions -->
      <div class="flex items-center gap-2.5">
        <!-- Dark/Light toggle -->
        <button
          @click="theme.toggle()"
          class="w-9 h-9 rounded-xl flex items-center justify-center border text-base transition-all duration-200 hover:scale-110 active:scale-95"
          style="background: var(--bg-elevated); border-color: var(--border-subtle)"
        >
          <span v-if="theme.isDark">☀️</span>
          <span v-else>🌙</span>
        </button>

        <!-- Avatar (logged in) -->
        <div
          v-if="!user.getIsLoggedOut"
          class="cursor-pointer"
          @click="showSubmenu = !showSubmenu"
        >
          <img
            class="w-10 h-10 rounded-xl object-cover ring-2 transition-all duration-200"
            :class="showSubmenu ? 'ring-violet-500' : 'ring-transparent hover:ring-violet-400'"
            :src="avatar"
            alt="Avatar"
          />
        </div>

        <!-- Auth buttons (logged out) -->
        <div v-else class="flex items-center gap-2">
          <RouterLink to="/login">
            <button
              class="px-4 py-2 text-sm font-medium rounded-xl border transition-all duration-200 hover:scale-105"
              style="color: var(--text-secondary); border-color: var(--border-default); background: transparent"
            >
              Login
            </button>
          </RouterLink>
          <RouterLink to="/register">
            <button
              class="px-4 py-2 text-sm font-semibold rounded-xl text-white transition-all duration-200 hover:scale-105"
              style="background: var(--gradient-brand)"
            >
              Sign Up
            </button>
          </RouterLink>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { computed, ref, watch } from "vue"
import { useUserStore } from "@/store/store"
import { useThemeStore } from "@/store/themeStore"
import { useRouter } from "vue-router"
import { getProfileImageSrc } from "@/composables/useProfileImage"

const showSubmenu = ref(false)
const user  = useUserStore()
const theme = useThemeStore()
const router = useRouter()
const searchTerm = ref('')

const avatar = computed(() => getProfileImageSrc(user.getUser?.profilePictureReference))

watch(() => searchTerm.value, (val) => {
  if (val.length > 2) {
    router.push('/search')
    user.fetchSearchResult(val)
  } else if (val.length === 0) {
    router.push('/')
  }
})

async function logOut() {
  showSubmenu.value = false
  await user.logOut()
  await router.push('/')
  location.reload()
}

function redirectToHome() {
  router.push('/')
}
</script>

<style scoped>
.gradient-text {
  background: var(--gradient-brand);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: 10px;
  font-size: 13.5px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s ease;
  color: var(--text-primary);
  text-decoration: none;
  border: none;
  background: transparent;
  width: 100%;
}
.dropdown-item:hover {
  background: var(--bg-elevated);
}
.logout-btn {
  color: #f87171;
}

.search-input:focus {
  border-color: #8b5cf6 !important;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.12);
}

.dropdown-enter-active,
.dropdown-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}
.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-6px) scale(0.97);
}
</style>
