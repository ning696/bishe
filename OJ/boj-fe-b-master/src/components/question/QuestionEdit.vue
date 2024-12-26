<template>
  <el-drawer v-model="visibleDrawer" :destroy-on-close="true" :title="title" direction="rtl" :with-header="false"
    size="60%">
    <span>{{ title }}</span>
    <el-form :model="formModel" ref="formRef" label-width="100px">
      <el-form-item label="题目标题:">
        <el-input style="width:387px !important" v-model="formQuestion.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item label="题目难度:">
        <selector style="width:387px !important" v-model="formQuestion.difficulty" width="100%" placeholder="请选择题目难度">
        </selector>
      </el-form-item>
      <el-form-item label="时间限制:">
        <el-input style="width:387px !important" v-model="formQuestion.timeLimit" placeholder="请输入时间限制"></el-input>
      </el-form-item>
      <el-form-item label="空间限制:">
        <el-input style="width:387px !important" v-model="formQuestion.spaceLimit" placeholder="请输入空间限制"></el-input>
      </el-form-item>
      <el-form-item label="题目内容:" style="font-weight: 700;">
        <div class="editor">
          <quill-editor placeholder="请输入题目内容" style="min-height:100px" v-model:content="formQuestion.content"
            contentType="html">
          </quill-editor>
        </div>
      </el-form-item>
      <el-form-item label="题目用例:">
        <el-input style="width:387px !important" v-model="formQuestion.questionCase" placeholder="请输入题目用例"></el-input>
        <!-- <div class="editor">
          <quill-editor theme="snow" v-model:content="formQuestion.questionCase" contentType="html">
          </quill-editor>
        </div> -->
      </el-form-item>
      <el-form-item label="默认代码块:" style="font-weight: 700;">
        默认代码块:
        <code-editor ref="defaultCodeRef" @update:value="handleEditorContent"></code-editor>
      </el-form-item>
      <el-form-item label="main函数:" style="font-weight: 700;">
        main函数:
        <code-editor ref="mainFuncRef" @update:value="handleEditorMainFunc"></code-editor>
      </el-form-item>
      <el-form-item>
        <el-button class="question-button" type="primary" plain @click="onSubmit()">发布</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<script setup>
import { reactive, ref } from 'vue'
import Selector from "./QuestionSelector.vue"
import codeEditor from '@/components/question/CodeEditor.vue'
import { QuillEditor } from '@vueup/vue-quill'
import { ElMessage } from 'element-plus'
import { addQuestionService, editQuestionService, getQuestionDetailService } from '@/apis/question'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

const visibleDrawer = ref(false)

const defaultForm = {
  title: '',
  difficulty: '',
  content: '',
  questionCase: '',
  timeLimit: '',
  spaceLimit: '',
  defaultCode: '',
  mainFunc: ''
}
const defaultCodeRef = ref(null)
const mainFuncRef = ref(null)
const formQuestion = ref({
  title: '',
  difficulty: '',
  content: '',
  questionCase: '',
  timeLimit: '',
  spaceLimit: '',
  defaultCode: '',
  mainFunc: ''
})

const handleEditorContent = (content) => {
  console.log("content: ", content)
  formQuestion.value.defaultCode = content
}

const handleEditorMainFunc = (content) => {
  console.log("content: ", content)
  formQuestion.value.mainFunc = content
}

const open = async (questionId) => {
  visibleDrawer.value = true
  formQuestion.value = { ...defaultForm }
  // formQuestion.value.defaultCode = ''
  // formQuestion.value.mainFunc = ''
  if (questionId) {
    console.log('编辑')
    const res = await getQuestionDetailService(questionId)
    formQuestion.value = { ...res.data }
    defaultCodeRef.value.setAceValue(formQuestion.value.defaultCode)
    mainFuncRef.value.setAceValue(formQuestion.value.mainFunc)
    formQuestion.value = { ...res.data }
  }
}
//向父级暴漏方法
defineExpose({
  open
})
const emit = defineEmits(['success'])
const validate = () => {
  let msg = ''
  if (!formQuestion.value.title) {
    msg = '请添加题目标题'
  } else if (formQuestion.value.difficulty == '') {
    msg = '请选择题目难度'
  } else if (!formQuestion.value.timeLimit) {
    msg = '请输入时间限制'
  } else if (!formQuestion.value.spaceLimit) {
    msg = '请输入空间限制'
  } else if (!formQuestion.value.content) {
    msg = '请输入题目内容信息'
  } else if (!formQuestion.value.questionCase) {
    msg = '请输入题目用例名称'
  } else if (!formQuestion.value.defaultCode) {
    msg = '请输入默认代码'
  } else if (!formQuestion.value.mainFunc) {
    msg = '请输入main函数'
  } else {
    msg = ''
  }
  return msg

}
const onSubmit = async () => {
  if (validate()) {
    ElMessage({
      message: validate(),
      type: 'error'
    })
    return false
  }
  const fd = new FormData()
  for (let key in formQuestion.value) {
    fd.append(key, formQuestion.value[key])
  }
  console.log(formQuestion)
  if (formQuestion.value.questionId) {
    //编辑
    await editQuestionService(fd)
    ElMessage.success('编辑成功')
    emit('success', 'edit')
  } else {
    //添加
    let res = await addQuestionService(fd)
    res && ElMessage.success('添加成功')
    emit('success', 'add')
  }
  visibleDrawer.value = false
}
</script>

<style lang="scss">
.question-button {
  width: 200px;
}
</style>