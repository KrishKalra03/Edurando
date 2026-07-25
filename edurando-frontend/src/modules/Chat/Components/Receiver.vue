<script setup>
defineProps(["item"])
</script>

<template>
  <div class="flex justify-start">
    <div class="msg-bubble received">
      <span class="msg-text">{{ item.contents }}</span>
    </div>
  </div>
</template>

<style scoped>
.msg-bubble {
  padding: 10px 14px;
  border-radius: 16px;
  border-bottom-left-radius: 4px;
  max-width: 70%;
  word-break: break-word;
}
.received {
  background: var(--bg-elevated);
  border: 1px solid var(--border-subtle);
  color: var(--text-primary);
  font-size: 14px;
}
</style>
