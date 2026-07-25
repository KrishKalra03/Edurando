<script setup>
defineProps(["item"])
</script>

<template>
  <div class="flex justify-end">
    <div class="msg-bubble sent">
      <span class="msg-text">{{ item.contents }}</span>
    </div>
  </div>
</template>

<style scoped>
.msg-bubble {
  padding: 10px 14px;
  border-radius: 16px;
  border-bottom-right-radius: 4px;
  max-width: 70%;
  word-break: break-word;
}
.sent {
  background: var(--gradient-brand);
  color: white;
  font-size: 14px;
}
</style>
