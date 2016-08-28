Ext.define('SystemApp.View.Intelligent', {
    extend: 'Ext.panel.Panel',
    xtype: 'intelligent',
    
    initComponent: function() {
    	Ext.apply(this, {
    		//region: 'center',
    		border: 0,
            layout: 'border',
            items: [this.createView()]
        });
    	this.createTextMesEditWindows();
    	this.createImageUpload();
    	this.createClassifyEditWindows();
    	this.createImageMesEditWindows();
    	this.loadAttentionDataForForm();
        this.callParent(arguments);
    },
    
    createView: function() {
    	
    	var TextMesStore = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name', 'descript', 'visible', 'url', 'markcode', 'content', 
    	            'modifyDate', 'viewcount'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/textMes.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	var ImageMesStore = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name', 'descript', 'level', 'img', 'visible', 'url', 
    	            'markcode', 'content', 'classify', 'isImg', 'modifyDate', 
    	            'viewcount', 'classifyName', 'smallimg'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/imageMes.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	var ClassifyStore = Ext.create('Ext.data.Store', {
    		pageSize: 25,
    	    fields:['name', 'descript', 'level', 'img', 'visible', 'url', 'markcode', 'smallimg'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/classify.do?list', 
    	        reader: {
    	            type: 'json',
    	            root: 'datas',
    	            totalProperty: 'total'
    	        }
    	    },
            autoLoad: true
    	});
    	
    	var markcodeStore = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"是", "value": true
    	    },{
    	    	"text":"否", "value": false
    	    }]
    	});
    	
    	
    	this.tabpanel = Ext.create('Ext.tab.Panel', {
    		region: 'center',
    	    items: [{
    	        title: '关注回复',
    	        layout: 'fit',
    	        items: [this.formAttentionPanel = Ext.widget({
    	            xtype: 'form',
    	            border: 0,
    	            autoScroll: true,
    	            region: 'center',
    		    	bodyPadding: 10,
    		    	fieldDefaults: {
    		            labelWidth: 120,
    		            labelAlign: 'left',
    		            anchor: '100%',
    		            labelStyle: 'color:green;padding-left:4px'
    		        },
    		        items: [{
    	        		xtype: 'hiddenfield',
    	        		name: 'id'
    	        	}, {
    	        		xtype:"container",
    		        	layout: 'column',
    		        	margin: 20,
    		        	items: [{
    			        	xtype: 'textarea',
    			        	name: 'content',
    			        	fieldLabel: '回复内容',
    			        	columnWidth: 0.6,
    			        	height: 200
    			        }, {
    		        		xtype: 'component',
    						html: '<span style="color:#aaa;margin-left:10px;">请填写回复的内容</span>'
    		        	}]
    	        	}, {
    	        		xtype:"container",
    		        	layout: 'column',
    		        	margin: 20,
    		        	items: [{
    			        	xtype: 'combo',
    			        	columnWidth: 0.6,
    			        	name: 'ismarkcode',
    			        	fieldLabel: '使用关键字',
    			        	store: markcodeStore,
    			            queryMode: 'local',
    			            valueField: 'value',
    			            displayField: 'text',
    			            editable: false,
    			            allowBlank: false,
    			            value: true
    			        }, {
    		        		xtype: 'component',
    						html: '<span style="color:#aaa;margin-left:10px;">是否使用关键字触发</span>'
    		        	}]
    	        	}, {
    	        		xtype:"container",
    		        	layout: 'column',
    		        	margin: 20,
    		        	items: [{
    			        	xtype: 'textfield',
    			            name: 'markcode',
    			            columnWidth: 0.6,
    			            fieldLabel: '关键字'
    			        }, {
    		        		xtype: 'component',
    						html: '<span style="color:#aaa;margin-left:10px;">请输入关键字</span>'
    		        	}]
    	        	}],
    		        dockedItems: [{
    		        	xtype: 'toolbar',
    	    			dock: 'top',
    	    			layout: 'fit',
    	    			items: [{
    	    				xtype:"container",
    	    				items: [{
    							xtype: 'component',
    							margin: 10,
    				            html: '<img src="" /><span style="color:#aaa">关注回复—用户关注公众号后即时推送本回复内容，也可以配置关键字再次触发推送</span>'
    	    				}, {
    	    					xtype:"toolbar",
    	                      	border: '0 0 1 0',
    	                      	items: [{
    	   	                      	xtype: 'button', 
    				            	text: '清空',
    				            	iconCls: 'icon-refresh',
    				            	scope: this,
    				            	handler: this.emptyAttentionFormData
    	                      	}, '-',{
    	   	                      	xtype: 'button', 
    				            	text: '重置',
    				            	iconCls: 'icon-turn',
    				            	scope: this,
    				            	handler: this.resetAttentionFormData
    	                      	}, '-', {
    	   	                      	xtype: 'button', 
    				            	text: '保存',
    				            	iconCls: 'icon-add',
    				            	scope: this,
    				            	handler: this.submitAttentionFormData
    	                      	}]
    	    				}]
    	    			}]
    		        }]
    	        })]
    	    }, {
    	        title: '文本回复',
    	        layout: 'fit',
    	        autoScroll: true,
    	        items: [this.TextMesGrid = Ext.create('Ext.grid.Panel', {
    	        	border: 0,
    	    	    store: TextMesStore,
    	    	    columnLines: true,
    	    	    emptyText: '暂无数据可显示',
    	    	    /*dockedItems: [{
        	        	xtype: 'pagingtoolbar',
        	        	store: TextMesStore,  
        	        	dock: 'bottom',
        	        	displayInfo: true
        	        }],*/
    	    	    dockedItems: [{
    	    	        xtype: 'pagingtoolbar',
    	    	        store: ImageMesStore,  
    	    	        dock: 'bottom',
    	    	        displayInfo: true
    	    	    }, {
    		        	xtype: 'toolbar',
    	    			dock: 'top',
    	    			layout: 'fit',
    	    			items: [{
    	    				xtype:"container",
    	    				items: [{
    							xtype: 'component',
    							margin: 10,
    				            html: '<img src="" /><span style="color:#aaa">文本回复—内容为纯文本形式推送给用户</span>'
    	    				}, {
    	    					xtype:"toolbar",
    	                      	border: '0 0 1 0',
    	                      	items: [{ 
    	        	            	xtype: 'button', 
    	        	            	text: '刷新',
    	        	            	iconCls: 'icon-refresh',
    	        	            	scope: this,
    	        	            	handler: this.refreshTextMesGird
    	        	            }, { 
    	        	            	xtype: 'button', 
    	        	            	text: '添加',
    	        	            	iconCls: 'icon-add',
    	        	            	scope: this,
    	        	            	handler: this.addTextMesGrid
    	        	            }, '->' ,{
    	        	            	xtype: 'textfield',
    	        	            	width: 180,
    	        	            	id: 'TextMes.SearchName'
    	        	            },{ 
    	        	            	xtype: 'button', 
    	        	            	text: '搜索',
    	        	            	iconCls: 'icon-search',
    	        	            	scope: this,
    	        	            	handler: this.searchTextMesGrid
    	        	            }]
    	    				}]
    	    			}]
    		        }],
    	    	    columns: [{
    	    	    	text: '序号',
    	    	    	xtype: 'rownumberer',
	    	    		width: 50
    	    	    }, { 
    	    	    	text: '文本名称',  
    	    	    	dataIndex: 'name',
    	    	    	flex: 0.15
    	    	    },{ 
    	    	    	text: '文本内容',  
    	    	    	dataIndex: 'content',
    	    	    	flex: 0.5
    	    	    }, {
    	    	    	text: '关键字',
    	    	    	dataIndex: 'markcode',
    	    	    	flex: 0.15
    	    	    }, {
    	    	    	text: '浏览次数',
    	    	    	dataIndex: 'viewcount',
    	    	    	flex: 0.15
    	    	    }, {
    	    	    	text: '修改时间',
    	    	    	dataIndex: 'modifyDate',
    	    	    	flex: 0.20,
    	    	    	renderer: function(v) {
    	    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d h:i');
    	    	    		console.info(v);
    	    	    	}
    	    	    }, {
    	    	    	text: '是否显示',
    	    	    	dataIndex: 'visible',
    	    	    	flex: 0.15,
    	    	    	renderer: function(v) {
    	                	if (v) return "<font color=green>显示</font>";
    	                	else return "<font color=red>隐藏</font>";
    	                }
    	    	    }, {
    	            	xtype: 'actiontextcolumn',
    	            	text: '主要操作',
    	            	flex: 0.25,
    	                items : [{  
    	                    text : "编辑",  
    	                    cls: 'icon-edit', 
    	                    scope: this,
    	                    handler: this.editTextMesGrid
    	                }, {  
    	                    text : "删除",  
    	                    cls: 'icon-del',
    	                    scope: this,
    	                    handler: this.delTextMesGrid
    	                }]  
    	            }]
    	    	})]
    	    }, {
    	        title: '图文回复',
    	        layout: {
    	            type: 'hbox',     //指定为hbox布局
    	            align: 'stretch'  //指定元素的高将充满容器的垂直空间
    	        },
    	        items: [{
    	            xtype: 'panel',
    	            title: '分类管理',
    	            flex: 1,
    	            layout: 'fit',
    	            items: [this.ClassifyGrid = Ext.create('Ext.grid.Panel', {
    	            border: 0,
    	            region: 'center',
    	            store: ClassifyStore,
    	            columnLines: true,
    	            emptyText: '暂无数据可显示',
        	        dockedItems: [{
    	    	        xtype: 'pagingtoolbar',
    	    	        store: ClassifyStore,  
    	    	        dock: 'bottom',
    	    	        displayInfo: true
    	    	    }, {
    		        	xtype: 'toolbar',
    	    			dock: 'top',
    	    			layout: 'fit',
    	    			items: [{
    	    				xtype:"container",
    	    				items: [{
    							xtype: 'component',
    							margin: 10,
    				            html: '<img src="" /><span style="color:#aaa">分类管理—用户使用关键字触发分类后，将把相应分类的图文信息以列表形式推送给用户</span>'
    	    				}, {
    	    					xtype:"toolbar",
    	                      	border: '0 0 1 0',
    	                      	items: [{ 
    	        	            	xtype: 'button', 
    	        	            	text: '刷新',
    	        	            	iconCls: 'icon-refresh',
    	        	            	scope: this,
    	        	            	handler: this.refreshClassifyGird
    	        	            }, { 
    	        	            	xtype: 'button', 
    	        	            	text: '添加',
    	        	            	iconCls: 'icon-add',
    	        	            	scope: this,
    	        	            	handler: this.addClassifyGrid
    	        	            }, '->' ,{
    	        	            	xtype: 'textfield',
    	        	            	width: 180,
    	        	            	emptyText: '输入名称搜索',
    	        	            	id: 'Classify.SearchName'
    	        	            }, { 
    	        	            	xtype: 'button', 
    	        	            	text: '搜索',
    	        	            	iconCls: 'icon-search',
    	        	            	scope: this,
    	        	            	handler: this.searchClassifyGrid
    	        	            }]
    	    				}]
    	    			}]
    		        }],
    		        columns: [{
    	    	    	text: '序号',
    	    	    	xtype: 'rownumberer',
	    	    		width: 50
    	    	    }, { 
    	    	    	text: '分类名称',  
    	    	    	dataIndex: 'name',
    	    	    	flex: 0.25
    	    	    }, {
    	    	    	text: '分类图片',
    	    	    	dataIndex: 'img',
    	    	    	flex: 0.15,
    	    	    	renderer: function(v, meta){
    	    	    		meta.tdAttr = "data-qtip='<img src=\"" + v + "\" />'";
    	    	    		return "查看图片";
    	    	    	}
    	    	    }, {
    	    	    	text: '关键字',
    	    	    	dataIndex: 'markcode',
    	    	    	flex: 0.15
    	    	    }, {
    	    	    	text: '等级排序',
    	    	    	dataIndex: 'level',
    	    	    	flex: 0.13
    	    	    }, {
    	    	    	text: '是否显示',
    	    	    	dataIndex: 'visible',
    	    	    	flex: 0.15,
    	    	    	renderer: function(v) {
    	                	if (v) return "<font color=green>显示</font>";
    	                	else return "<font color=red>隐藏</font>";
    	                }
    	    	    }, {
    	            	xtype: 'actiontextcolumn',
    	            	text: '主要操作',
    	            	flex: 0.25,
    	                items : [{  
    	                    text : "编辑",  
    	                    cls: 'icon-edit', 
    	                    scope: this,
    	                    handler: this.editClassifyGrid
    	                }, {  
    	                    text : "删除",  
    	                    cls: 'icon-del',
    	                    scope: this,
    	                    handler: this.delClassifyGrid
    	                }]  
    	            }]
    	            })]
    	        }, {
    	            xtype: 'panel',
    	            title: '图文回复',
    	            flex: 1,
    	            layout: 'fit',
    	            items: [this.ImageMesGrid = Ext.create('Ext.grid.Panel', {
    	            	border: 0,
    	            	region: 'center',
        	    	    columnLines: true,
        	    	    emptyText: '暂无数据可显示',
        	    	    store: ImageMesStore,
        	    	    dockedItems: [{
        	    	        xtype: 'pagingtoolbar',
        	    	        store: ImageMesStore,  
        	    	        dock: 'bottom',
        	    	        displayInfo: true
        	    	    }, {
        		        	xtype: 'toolbar',
        	    			dock: 'top',
        	    			layout: 'fit',
        	    			items: [{
        	    				xtype:"container",
        	    				items: [{
        							xtype: 'component',
        							margin: 10,
        				            html: '<img src="" /><span style="color:#aaa">图文回复—用户使用关键字触发后，以单个图文内容形式推送给用户</span>'
        	    				}, {
        	    					xtype:"toolbar",
        	                      	border: '0 0 1 0',
        	                      	items: [{ 
        	        	            	xtype: 'button', 
        	        	            	text: '刷新',
        	        	            	iconCls: 'icon-refresh',
        	        	            	scope: this,
        	        	            	handler: this.refreshImageMesGrid
        	        	            }, { 
        	        	            	xtype: 'button', 
        	        	            	text: '添加',
        	        	            	iconCls: 'icon-add',
        	        	            	scope: this,
        	        	            	handler: this.addImageMesGrid
        	        	            }, '->' ,{
        	        	            	xtype: 'textfield',
        	        	            	width: 180,
        	        	            	emptyText: '输入名称搜索',
        	        	            	id: 'ImageMes.SearchName'
        	        	            },{ 
        	        	            	xtype: 'button', 
        	        	            	text: '搜索',
        	        	            	iconCls: 'icon-search',
        	        	            	scope: this,
        	        	            	handler: this.searchImageMesGrid
        	        	            }]
        	    				}]
        	    			}]
        		        }],
        	    	    columns: [{
        	    	    	text: '序号',
        	    	    	xtype: 'rownumberer',
    	    	    		width: 50
        	    	    }, {
        	    	    	text: '图文分类',
        	    	    	dataIndex: 'classifyName',
        	    	    	flex: 0.25
        	    	    }, { 
        	    	    	text: '图文名称',  
        	    	    	dataIndex: 'name',
        	    	    	flex: 0.20
        	    	    }, {
        	    	    	text: '关键字',
        	    	    	dataIndex: 'markcode',
        	    	    	flex: 0.15
        	    	    }/*, {
        	    	    	text: '浏览次数',
        	    	    	dataIndex: 'viewcount',
        	    	    	flex: 0.15
        	    	    }, {
        	    	    	text: '修改时间',
        	    	    	dataIndex: 'modifyDate',
        	    	    	flex: 0.20,
        	    	    	renderer: function(v) {
        	    	    		return Ext.Date.format(new Date(v.time), 'Y-m-d h:i');
        	    	    		console.info(v);
        	    	    	}
        	    	    }*/, {
        	    	    	text: '等级排序',
        	    	    	dataIndex: 'level',
        	    	    	flex: 0.15
        	    	    }, {
        	    	    	text: '是否显示',
        	    	    	dataIndex: 'visible',
        	    	    	flex: 0.15,
        	    	    	renderer: function(v) {
        	                	if (v) return "<font color=green>显示</font>";
        	                	else return "<font color=red>隐藏</font>";
        	                }
        	    	    }, {
        	            	xtype: 'actiontextcolumn',
        	            	text: '主要操作',
        	            	flex: 0.25,
        	                items : [{  
        	                    text : "编辑",  
        	                    cls: 'icon-edit', 
        	                    scope: this,
        	                    handler: this.editImageMesGrid
        	                }, {  
        	                    text : "删除",  
        	                    cls: 'icon-del',
        	                    scope: this,
        	                    handler: this.delImageMesGrid
        	                }]  
        	            }]
    	            })]
    	            
    	        }]
    	    }]
    	});
    	return this.tabpanel;
    },
    
    /************************************** 关注回复 **************************************/ 
    
   resetAttentionFormData: function() {
   		this.loadAttentionDataForForm();
   },
   
   emptyAttentionFormData: function() {
	   console.log(this.formAttentionPanel.getForm());
	   this.formAttentionPanel.getForm().reset();
  },
  
  loadAttentionDataForForm: function() {
  	var form = this.formAttentionPanel.getForm();
  	form.reset();
  	Ext.Ajax.request({
  	    url: '/admin/attention.do?info',
  	    success: function(response){
  	    	var res = Ext.decode(response.responseText);
  	    	if(res != null) {
	  	    	form.findField('ismarkcode').setValue(res.ismarkcode);
	  	    	form.findField('content').setValue(res.content);
	  	    	form.findField('markcode').setValue(res.markcode);
  	    	}
  	    },
  	    failure: function(response) {
  	    	Ext.Msg.alert('提示', '操作异常');
  	    }
  	});
  },
  
  submitAttentionFormData: function() {
  	var form = this.formAttentionPanel.getForm();
  	form.submit({
  		url: '/admin/attention.do?save',
  	    success: function(form, action) {
  	       Ext.Msg.alert('提示', action.result.title);
  	    },
  	    failure: function(form, action) {
  	    	Ext.Msg.alert('提示', '操作异常');
  	    }
  	});
  },
    
 

    /************************************** 文本回复 **************************************/    
    
    refreshTextMesGird: function() {
    	this.TextMesGrid.getStore().reload();
    },
    
    searchTextMesGrid: function() {
    	var store = this.grid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('TextMes.SearchName').getValue()
		};
		store.load();
    },
    
    createTextMesEditWindows: function() {
    	
    	var visible = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	this.TextMesEditWin = Ext.create('Ext.window.Window', {
    	    title: '编辑文本信息',
    	    height: 400,
    	    width: 800,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	autoScroll: true,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '文本名称',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'markcode',
    	            fieldLabel: '关键字'
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'visible',
    	        	fieldLabel: '是否显示',
    	        	store: visible,
    	            queryMode: 'local',
    	            valueField: 'value',
    	            displayField: 'text',
    	            editable: false,
    	            allowBlank: false,
    	            value: true
    	        }, {
    	        	xtype: 'textarea',
    	        	name: 'content',
    	        	fieldLabel: '文本内容',
    	        	height: 150
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.TextMesSubmitFormData
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.TextMesEditWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    addTextMesGrid: function() {
		var form = this.TextMesEditWin.down('form');
    	this.TextMesEditWin.show();
    	form.getForm().reset();
    },
    
    editTextMesGrid: function(grid, rowIndex, colIndex) {
    	var form = this.TextMesEditWin.down('form');
    	this.TextMesEditWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    TextMesSubmitFormData: function() {
    	var grid  = this.TextMesGrid;
    	var form = this.TextMesEditWin.down('form');
    	var win = this.TextMesEditWin;
    	form.submit({
    		url: '/admin/textMes.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delTextMesGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.TextMesGrid;
		Ext.MessageBox.confirm('确认', '是否删除该文本信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/textMes.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
   /************************************** 分类管理 **************************************/ 
    
    refreshClassifyGird: function() {
    	this.ClassifyGrid.getStore().reload();
    },
    
    searchClassifyGrid: function() {
    	var store = this.ClassifyGrid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('Classify.SearchName').getValue()
		};
		store.load();
    },
    
    createClassifyEditWindows: function() {
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	
    	this.editClassifyWin = Ext.create('Ext.window.Window', {
    	    title: '编辑分类信息',
    	    height: 400,
    	    width: 700,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '分类名称',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'descript',
    	            fieldLabel: '分类描述'
    	        }, {
    	        	xtype: 'numberfield',
    	        	name: 'level',
    	        	fieldLabel: '等级排序',
    	            minValue: 0,
    	            maxValue: 1000,
    	            allowBlank: false,
    	            value: 0
    	        }, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	style: 'margin-bottom:5px',
    	        	id: 'ImageMes_Container_ClassifySmallimg',
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'smallimg',
        	            fieldLabel: '分类图标',
        	            columnWidth: 0.7,
        	            listeners: {
        	            	change: this.scanImage
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.15,
    	        		style: 'margin-left:3px',
    	        		id: 'ImageMes_Upload_ClassifySmallimg',
    	        		scope: this,
    	        		handler: this.uploadImage
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.15,
    	        		scope: this,
    	        		style: 'margin-left:3px'
    	        	}]
    	        }, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	style: 'margin-bottom:5px',
    	        	id: 'ImageMes_Container_ClassifyImg',
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'img',
        	            fieldLabel: '分类图片',
        	            columnWidth: 0.7,
        	            listeners: {
        	            	change: this.scanImage
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.15,
    	        		style: 'margin-left:3px',
    	        		id: 'ImageMes_Upload_ClassifyImg',
    	        		scope: this,
    	        		handler: this.uploadImage
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.15,
    	        		scope: this,
    	        		style: 'margin-left:3px'
    	        	}]
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'url',
    	            fieldLabel: '链接地址'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'markcode',
    	            fieldLabel: '关键字'
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'visible',
    	        	fieldLabel: '是否显示',
    	        	store: enabled,
    	            queryMode: 'local',
    	            valueField: 'value',
    	            displayField: 'text',
    	            editable: false,
    	            allowBlank: false,
    	            value: true
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.submitClassifyFormDate
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.editClassifyWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
    addClassifyGrid: function() {
		var form = this.editClassifyWin.down('form');
    	this.editClassifyWin.show();
    	form.getForm().reset();
    },
    
    editClassifyGrid: function(grid, rowIndex, colIndex) {
    	var form = this.editClassifyWin.down('form');
    	this.editClassifyWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    submitClassifyFormDate: function() {
    	var grid  = this.ClassifyGrid;
    	var form = this.editClassifyWin.down('form');
    	var win = this.editClassifyWin;
    	form.submit({
    		url: '/admin/classify.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delClassifyGrid: function(grid, rowIndex, colIndex) {
    	var grid  = this.ClassifyGrid;
    	var record = grid.getStore().getAt(rowIndex);
		Ext.MessageBox.confirm('确认', '是否删除该分类信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/classify.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    
    
    
   /************************************** 图文回复 **************************************/ 
    
    refreshImageMesGrid: function() {
    	this.ImageMesGrid.getStore().reload();
    },
    
    searchImageMesGrid: function() {
    	var store = this.ImageMesGrid.getStore();
    	store.currentPage = 1;
    	store.proxy.extraParams = {
			name: Ext.getCmp('ImageMes.SearchName').getValue()
		};
		store.load();
    },
    
    createImageMesEditWindows: function() {
    	
    	var enabled = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	var visible = Ext.create('Ext.data.Store', {
    	    fields: ['text', 'value'],
    	    data : [{
    	    	"text":"显示", "value": true
    	    },{
    	    	"text":"隐藏", "value": false
    	    }]
    	});
    	
    	this.classifyImgStore = Ext.create('Ext.data.Store', {
    	    fields: ['name', 'id'],
    	    proxy: {
    	        type: 'ajax',
    	        url: '/admin/imageMes.do?cls'
    	    },
            autoLoad: true
    	});
    	
    	this.ImageMesEditWin = Ext.create('Ext.window.Window', {
    	    title: '编辑图文信息',
    	    height: 600,
    	    width: 900,
    	    layout: 'border',
    	    modal: true,
    	    closeAction: 'hide',
    	    items: [{  
    	    	xtype: 'form',
    	    	region: 'center',
    	    	bodyPadding: 10,
    	    	autoScroll: true,
    	    	fieldDefaults: {
    	            labelWidth: 75,
    	            labelAlign: 'left',
    	            anchor: '100%'
    	        },
    	        items: [{
    	        	xtype: 'hiddenfield',
    	        	name: 'id'
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'classify',
    	        	fieldLabel: '所属分类',
    	            store: this.classifyImgStore,
    	            queryMode: 'remote',
    	            displayField: 'name',
    	            valueField: 'id',
    	            editable: false,
    	            allowBlank: false
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'name',
    	            fieldLabel: '图文名称',
    	            allowBlank: false
    	        }, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	style: 'margin-bottom:5px',
    	        	id: 'ImageMes_Container_ImageMesSmallimg',
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'smallimg',
        	            fieldLabel: '图文图标',
        	            columnWidth: 0.7,
        	            listeners: {
        	            	change: this.scanImage
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.15,
    	        		style: 'margin-left:3px',
    	        		id: 'ImageMes_Upload_ImageMesSmallimg',
    	        		scope: this,
    	        		handler: this.uploadImage
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.15,
    	        		scope: this,
    	        		style: 'margin-left:3px'
    	        	}]
    	        }, {
    	        	xtype: 'container',
    	        	layout: 'column',
    	        	style: 'margin-bottom:5px',
    	        	id: 'ImageMes_Container_ImageMesImg',
    	        	items: [{
    	        		xtype: 'textfield',
        	            name: 'img',
        	            fieldLabel: '图文图片',
        	            columnWidth: 0.7,
        	            listeners: {
        	            	change: this.scanImage
        	            }
    	        	}, {
    	        		xtype: 'button',
    	        		text: '上传图片',
    	        		columnWidth: 0.15,
    	        		style: 'margin-left:3px',
    	        		id: 'ImageMes_Upload_ImageMesImg',
    	        		scope: this,
    	        		handler: this.uploadImage
    	        	}, {
    	        		xtype: 'button',
    	        		text: '预览图片',
    	        		columnWidth: 0.15,
    	        		scope: this,
    	        		style: 'margin-left:3px'
    	        	}]
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'isImg',
    	        	fieldLabel: '显示封面',
    	            store: enabled,
    	            queryMode: 'remote',
    	            displayField: 'text',
    	            valueField: 'value',
    	            editable: false,
    	            allowBlank: false,
    	            value: true
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'markcode',
    	            fieldLabel: '关键字'
    	        }, {
    	        	xtype: 'textfield',
    	            name: 'url',
    	            fieldLabel: '链接地址'
    	        }, {
    	        	xtype: 'numberfield',
    	        	name: 'level',
    	        	fieldLabel: '等级排序',
    	            minValue: 0,
    	            maxValue: 1000,
    	            allowBlank: false,
    	            value: 0
    	        }, {
    	        	xtype: 'combo',
    	        	name: 'visible',
    	        	fieldLabel: '是否显示',
    	        	store: visible,
    	            queryMode: 'local',
    	            valueField: 'value',
    	            displayField: 'text',
    	            editable: false,
    	            allowBlank: false,
    	            value: true
    	        }, {
    	        	xtype: 'textarea',
    	        	name: 'descript',
    	        	fieldLabel: '图文简介',
    	        	height: 150
    	        }, {
    	        	xtype: 'kindeditor',
    	        	name: 'content',
    	        	fieldLabel: '图文内容',
    	        	height: 350
    	        }],
    	        buttons: [{ 
    	        	text: '确定',
    	        	scope: this,
    	        	handler: this.ImageMesSubmitFormData
    	        }, { 
    	        	text: '取消',
    	        	scope: this,
    	        	handler: function() {
    	        		this.ImageMesEditWin.hide();
    	        	}
    	        }]
    	    }]
    	});
    },
    
	    createImageUpload: function() {
	    this.uploadImageWin = Ext.create('Ext.window.Window', {
		    title: '上传图片',
		    height: 150,
		    width: 400,
		    layout: 'border',
		    modal: true,
		    closeAction: 'hide',
		    items: [{  
		    	xtype: 'form',
		    	region: 'center',
		    	bodyPadding: 10,
		    	fieldDefaults: {
		            labelWidth: 75,
		            labelAlign: 'left',
		            anchor: '100%'
		        },
		        items: [{
		        	xtype: 'hiddenfield',
		        	name: 'id'
		        }, {
		        	xtype: 'hiddenfield',
		        	name: 'img'
		        }, {
		            xtype: 'filefield',
		            name: 'upload',
		            fieldLabel: '图片文件',
		            msgTarget: 'side',
		            buttonText: '选择图片...'
		        }],
		        buttons: [{ 
		        	text: '确定',
		        	scope: this,
		        	handler: this.uploadImageForm
		        }, { 
		        	text: '取消',
		        	scope: this,
		        	handler: function() {
		        		this.uploadImageWin.hide();
		        	}
		        }]
		    }]
		});
	},

	scanImage: function(fn) {
	 	fn.up('container').items.get(2).setTooltip(
	    	'<img src="' + fn.up('container').items.get(0).getValue() + '"  width="100%"/>'
		);
	},
	
	uploadImage: function(fn) {
		var form = this.uploadImageWin.down('form');
		this.uploadImageWin.show();
		form.getForm().reset();
		form.getForm().findField('id').setValue(fn.up('container').items.get(0).getName( ));
		form.getForm().findField('img').setValue(fn.up('container').items.get(0).id);
	}, 
	
	uploadImageForm: function(fn) {
		var form = this.uploadImageWin.down('form');
		var target = Ext.getCmp(form.getForm().findField('img').getValue());
		var targetName = form.getForm().findField('id').getValue();  
		var win = this.uploadImageWin;
		form.submit({
			url: '/admin/upload.do?save',
		    success: function(form, action) {
		       target.setValue(action.result.msg);
		       win.hide();
		       Ext.example.msg('提示', action.result.title);
		    },
		    failure: function(form, action) {
		    	Ext.example.msg('提示', action.result.title);
		    }
		});
	},
    
    addImageMesGrid: function() {
		var form = this.ImageMesEditWin.down('form');
    	this.ImageMesEditWin.show();
    	form.getForm().reset();
    	this.classifyImgStore.reload();
    },
    
    editImageMesGrid: function(grid, rowIndex, colIndex) {
    	var form = this.ImageMesEditWin.down('form');
    	this.ImageMesEditWin.show();
    	var rec = grid.getStore().getAt(rowIndex);
    	form.loadRecord(rec);
    },
    
    ImageMesSubmitFormData: function() {
    	var grid  = this.ImageMesGrid;
    	var form = this.ImageMesEditWin.down('form');
    	var win = this.ImageMesEditWin;
    	form.submit({
    		url: '/admin/imageMes.do?save',
    	    success: function(form, action) {
    	       win.hide();
    	       Ext.Msg.alert('提示', action.result.title);
    	       grid.getStore().reload();
    	    },
    	    failure: function(form, action) {
    	    	Ext.Msg.alert('提示', action.result.title);
    	    }
    	});
    },
    
    delImageMesGrid: function(grid, rowIndex, colIndex) {
    	var record = grid.getStore().getAt(rowIndex);
    	var grid  = this.ImageMesGrid;
		Ext.MessageBox.confirm('确认', '是否删除该图文信息?', function(btn) {
    		if (btn != 'yes') return;
    		Ext.Ajax.request({
        	    url: '/admin/imageMes.do?delete',
        	    params: {
        	        id: record.get('id')
        	    },
        	    success: function(response){
        	    	var res = Ext.decode(response.responseText);
        	    	if (res.success) {
        	    		grid.getStore().reload();
        	    	}
        	    	Ext.Msg.alert('提示', res.title);
        	    },
        	    failure: function(response) {
        	    	var res = Ext.decode(response.responseText);
        	    	Ext.Msg.alert('提示', res.title);
        	    }
        	});
		});
    },
    
    
    onDestroy: function(){
    	this.ImageMesEditWin.destroy();
    	this.uploadImageWin.destroy();
    	this.TextMesEditWin.destroy();
    	this.editClassifyWin.destroy();
        this.callParent(arguments);
    }
});