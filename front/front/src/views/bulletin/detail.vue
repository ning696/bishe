<template>
  <div class="announcement-container">
    <h2 class="announcement-title">{{ title }}</h2>
    <div class="announcement-preview" v-html="content"></div>
  </div>
</template>

<script>
import { getAnnouncement } from '../../api/announcement'

export default {
  data() {
    return {
      bulletinId: '',
      title: '',
      content: ''
    }
  },
  created() {
    this.parseAnnouncement()
  },
  methods: {
    parseAnnouncement() {
      if (this.$route.params.bulletId === undefined) {
        this.bulletinId = localStorage.getItem('bulletinId')
      } else {
        localStorage.setItem('bulletinId', this.$route.params.bulletId)
        this.bulletinId = localStorage.getItem('bulletinId')
      }
      getAnnouncement(this.bulletinId).then(response => {
        this.title = response.data.data.title
        this.content = response.data.data.content
      })
    }
  }
}
</script>

<style scoped>
.announcement-container {
  max-width: 800px; /* 最大宽度 */
  margin: 0 auto; /* 居中 */
  padding: 20px;
  background-color: #ffffff; /* 背景颜色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 盒子阴影 */
  border-radius: 8px; /* 圆角 */
}

.announcement-title {
  font-size: 1.8em; /* 标题字体大小 */
  font-weight: bold; /* 加粗 */
  color: #333; /* 字体颜色 */
  margin-bottom: 15px; /* 底部间距 */
  text-align: center; /* 居中对齐 */
}

.announcement-preview {
  font-size: 1em; /* 内容字体大小 */
  color: #555; /* 字体颜色 */
  line-height: 1.6; /* 行高 */
  border: 1px solid #dcdfe6; /* 边框 */
  padding: 20px; /* 内边距 */
  background-color: #f9f9f9; /* 背景颜色 */
  margin-top: 20px; /* 顶部间距 */
  border-radius: 6px; /* 圆角 */
}

/* 响应式设计 */
@media (max-width: 600px) {
  .announcement-container {
    padding: 15px;
  }

  .announcement-title {
    font-size: 1.6em;
  }

  .announcement-preview {
    font-size: 0.9em;
  }
}
</style>
