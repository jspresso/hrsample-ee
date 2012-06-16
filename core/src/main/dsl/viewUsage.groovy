// Implement your views here using the SJS DSL.
border('MUStat.module.view', borderType:'TITLED', i18nNameKey:'MU.application.usage.title') {
  north {

    // selection part
    form (model:'MUStat', columnCount:2) {
      fields {
        propertyView name:'period'
        referencePropertyView name:'workspace', lovAction:'lovAction'
      }
    }
  }
  center {

    // the statistics part
    border (borderType:'TITLED', i18nNameKey:'MU.distribution.title') {

      north {
        // two polar charts
        evenGrid (drivingDimension:'ROW', drivingCellCount:2) {
          cells {
            cell {
              // bar series chart for users count per module
              border {
                north {
                  form {
                    fields {
                      propertyView name:'usersCount', readOnly:true, action:'exportApplicationUsgeFrontAction'
                    }
                  }
                }
                center {
                  cartesianChart (model:'MUStat-usersPerModule', label:'label', borderType:'NONE', legend:false) {
                    barSeries (valueField:'count', action:'muSelectModuleFrontAction')
                  }
                }
              }
            }

            cell {
              // polar chart for access count
              border {
                north {
                  form {
                    fields {
                      propertyView name:'accessCount', readOnly:true, action:'exportApplicationUsgeFrontAction'
                    }
                  }
                }
                center {
                  polarChart (model:'MUStat-accessPerModule', label:'label', borderType:'NONE', preferredHeight:300, preferredWidth:300) {

                    pieSeries (valueField:'count', action:'muSelectModuleFrontAction')
                  }
                }
              }
            }
          }
        }
      }
      center {
        // Historical details for selected model
        border {
          north {
            form () {
              fields {
                referencePropertyView name:'historyModule', lovAction:'lovAction'
              }
            }
          }
          center {
            cartesianChart (model:'MUStat-historyDetails', label:'label', borderType:'NONE', legend:false) {
               lineSeries (valueField:'count')
            }
          }
        }
      }
    }
  }
}

action ('exportApplicationUsgeFrontAction', 
  class:'org.jspresso.framework.application.frontend.action.FrontendAction')

action ('muSelectModuleFrontAction',
  class:'org.jspresso.hrsample.ext.frontend.usage.SelectModuleFrontAction')

