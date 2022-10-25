package com.blog.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.ExcelCategoryVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取用户列表
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Category category,Integer pageNum,Integer pageSize){
        return categoryService.selectCategoryPage(category,pageNum,pageSize);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Category category){
        return categoryService.add(category);
    }

    /**
     * 获取分类信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id){
        return categoryService.getInfo(id);
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Category category){
        return categoryService.edit(category);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseResult remove(@PathVariable(value = "id")Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 查询所有分类
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        /*List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);*/
        return categoryService.listAllCategory();
    }

    /**
     * 导出Excel
     *文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     * @param response
     */
    @PreAuthorize("@ps.haspermission('content:category:export')")
//    @PostAuthorize()
    @GetMapping("/export")
    public void export(HttpServletResponse response) {

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);

            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            //转换集合  Category转成ExcelCategoryVo
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);

            //把数据写入到Excel中   这里需要设置不关闭流 autoCloseStream(Boolean.FALSE)
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            e.printStackTrace();
            //如果出现异常也要响应json数据
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response,JSON.toJSONString(result));
        }



    }

}
