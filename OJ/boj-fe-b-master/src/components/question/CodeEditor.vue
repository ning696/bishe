<template>
  <div ref="editorform" style="height: 400px" class="ace-editor">
  </div>
</template>


<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from "vue";
import ace from "ace-builds";
// import "ace-builds/webpack-resolver";
import * as beautify from "ace-builds/src-noconflict/ext-beautify";
import "ace-builds/src-noconflict/mode-yaml";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/mode-c_cpp";
import "ace-builds/src-noconflict/mode-golang";
import "ace-builds/src-noconflict/theme-chaos";
import "ace-builds/src-noconflict/theme-eclipse";
import "ace-builds/src-noconflict/ext-language_tools";
import "ace-builds/src-noconflict/ext-emmet";
import "ace-builds/src-noconflict/snippets/yaml";

// 创建响应式引用
const editorform = ref(null);
let editor = null;
const emit = defineEmits(['update:value']);

// 定义选项
const options = {
  theme: `ace/theme/eclipse`,
  mode: `ace/mode/java`,
  tabSize: 2,
  maxLines: 25,
  minLines: 25,
  showPrintMargin: false,
  fontSize: 15,
  readOnly: false,
};
// const props = defineProps({
//   defaultCode: {
//     type: String,
//     default: () => {
//       return 'public class'
//     }
//   }
// })
// watch(props.defaultCode, (defaultCode, prevDefaultCode) => {
//   // console.log('defaultCode', defaultCode, 'olddefaultCode', prevDefaultCode)
//   editor = ace.edit(editorform.value, options);
//   editor.setValue(props.defaultCode)
// })
// 初始化编辑器
onMounted(() => {
  editor = ace.edit(editorform.value, options);
  // setTimeout(() => {
  //   editor.setValue(props.defaultCode)
  // }, 100)
  editor.setOptions({
    enableSnippets: true,
    enableLiveAutocompletion: true,
    enableBasicAutocompletion: true,
  });
  editor.getSession().setUseWrapMode(true);

  editor.getSession().on('change', () => {
    // 当编辑器内容变化时，触发自定义事件并传递编辑器的内容
    emit('update:value', editor.getValue());
  });
});

function setAceValue(aceCode) {
  console.log('aceCode:', aceCode)
  editor.setValue(aceCode)
}

defineExpose({
  setAceValue
})

// 销毁编辑器实例
onBeforeUnmount(() => {
  if (editor) {
    editor.destroy();
    editor = null;
  }
});
</script>

<style lang="scss" scoped>
.ace-editor {
  margin: 10px 0 0 0;
  width: 100%;
}
</style>