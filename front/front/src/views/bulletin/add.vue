<template>
  <div>
    <el-form label-width="100px">
      <!-- 新增标题输入框 -->
      <el-form-item label="文章标题">
        <el-input
          v-model="title"
          maxlength="50"
          show-word-limit
          placeholder="请输入文章标题（5到50个字）"
        />
      </el-form-item>

      <!-- 公告内容富文本编辑器 -->
      <el-form-item label="公告内容">
        <quill-editor
          ref="myQuillEditor"
          v-model="content"
          :options="editorOptions"
          style="height: 500px;"
          @blur="onEditorBlur"
          @focus="onEditorFocus"
          @change="onEditorChange"
        />
      </el-form-item>
    </el-form>

    <!-- 固定在底部的按钮 -->
    <div class="fixed-bottom-buttons">
      <el-button @click="setStatus('草稿')">保存为草稿</el-button>
      <el-button type="primary" @click="setStatus('已发布')">立即发布</el-button>
    </div>
  </div>
</template>

<script>
// 引入必要的样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import { quillEditor } from 'vue-quill-editor/src'
import { addAnnouncement } from '../../api/announcement'

export default {
  components: {
    quillEditor
  },
  data() {
    return {
      title: '', // 用于存储公告标题
      content: '', // 用于存储公告内容
      editorOptions: {
        placeholder: '请输入公告内容...',
        theme: 'snow' // 可选主题 'snow' 或 'bubble'
      },
      form: {}
    }
  },
  methods: {
    onEditorBlur() {
      console.log('编辑器失去焦点')
    },
    onEditorFocus() {
      console.log('编辑器获得焦点')
    },
    onEditorChange() {
      console.log('内容变化：', this.content)
    },
    setStatus(status) {
      if (this.title.length < 5 || this.title.length > 50) {
        this.$message.error('标题字数应在5到50个字之间')
        return
      }
      if (this.content.length === 0 || this.content.trim() === '') {
        this.$message.error('公告内容不能为空')
        return
      }
      if (this.content.length < 10) {
        this.$message.error('公告内容字数应不少于10个字')
        return
      }
      this.form.title = this.title
      this.form.content = this.content
      this.form.status = status
      addAnnouncement(this.form)
    }
  }
}
</script>

<style scoped>
/* 固定在页面底部的按钮容器 */
.fixed-bottom-buttons {
  position: fixed;
  bottom: 20px;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 10px;
  padding: 10px;
  background-color: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  border-top: 1px solid #ebeef5; /* 上边框 */
  z-index: 1000; /* 确保在最上层 */
}
</style>
