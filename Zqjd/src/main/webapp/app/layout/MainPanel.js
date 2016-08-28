Ext.define('FeedViewer.View.Layout.MainPanel', {
    
    extend: 'Ext.tab.Panel',
    alias: 'widget.layout.mainpanel',
    
    maxTabWidth: 230,

    initComponent: function() {
        this.callParent();
    },
    
    addPanel: function(name, nspace){
    	var active = this.getComponent('Tab_' + nspace);
    	if (active == undefined) {
    		active = this.add({
         	   xtype: 'panel',
         	   closable: true,
         	   title: name,
         	   layout: 'border',
         	   id: 'Tab_' + nspace
            });
            var alias = 'widget.' + nspace, cmp;
            if (nspace) {
                var className = Ext.ClassManager.getNameByAlias(alias);
                var ViewClass = Ext.ClassManager.get(className);

                cmp = new ViewClass({region: 'center'});
                active.add(cmp);
                if (cmp.floating) {
                    cmp.show();
                } 
//                else {
//                    this.centerContent(cmp);
//                }
            }
    	}
        this.setActiveTab(active);
    },
    
    centerContent: function(active) {
        var contentPanel = active,
            body = contentPanel.body,
            item = contentPanel.items.getAt(0),
            align = 'c-c',
            overflowX,
            overflowY,
            offsets;

        if (item) {
            overflowX = (body.getWidth() < (item.getWidth() + 40));
            overflowY = (body.getHeight() < (item.getHeight() + 40));

            if (overflowX && overflowY) {
                align = 'tl-tl';
                offsets = [20, 20];
            } else if (overflowX) {
                align = 'l-l';
                offsets = [20, 0];
            } else if (overflowY) {
                align = 't-t';
                offsets = [0, 20];
            }

            item.alignTo(contentPanel.body, align, offsets);
        }
    } ,
    
    /**
     * Listens for a new tab request
     * @private
     * @param {FeedViewer.FeedPost} The post
     * @param {Ext.data.Model} model The model
     */
    onTabOpen: function(post, rec) {
        var items = [],
            item,
            title;
            
        if (Ext.isArray(rec)) {
            Ext.each(rec, function(rec) {
                title = rec.get('title');
                if (!this.getTabByTitle(title)) {
                    items.push({
                        inTab: true,
                        xtype: 'feedpost',
                        title: title,
                        closable: true,
                        data: rec.data,
                        active: rec
                    });
                }
            }, this);
            this.add(items);
        }
        else {
            title = rec.get('title');
            item = this.getTabByTitle(title);
            if (!item) {
                item = this.add({
                    inTab: true,
                    xtype: 'feedpost',
                    title: title,
                    closable: true,
                    data: rec.data,
                    active: rec
                });
            }
            this.setActiveTab(item);
        }
    },

    /**
     * Find a tab by title
     * @param {String} title The title of the tab
     * @return {Ext.Component} The panel matching the title. null if not found.
     */
    getTabByTitle: function(title) {
        var index = this.items.findIndex('title', title);
        return (index < 0) ? null : this.items.getAt(index);
    },
    
    /**
     * Listens for a row dblclick
     * @private
     * @param {FeedViewer.Detail} detail The detail
     * @param {Ext.data.Model} model The model
     */
    onRowDblClick: function(info, rec){
        this.onTabOpen(null, rec);
    },
    
    /**
     * Listens for the open all click
     * @private
     * @param {FeedViewer.FeedDetail}
     */
    onOpenAll: function(detail) {
        this.onTabOpen(null, detail.getFeedData());
    }
});