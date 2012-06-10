// Implement your views here using the SJS DSL.
border('MUStat.module.view', borderType:'TITLED', i18nNameKey:'MU.application.usage.title') {
  north {

    // selection part
    form (model:'MUStat', columnCount:2) {
      fields {
        propertyView name:'period'
        referencePropertyView name:'workspace', lovAction:'lovFindFrontAction'
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
              // polar chart for users count
              border {
                north {
                  form {
                    fields {
                      propertyView name:'usersCount', readOnly:true
                    }
                  }
                }
                center {
                  polarChart (model:'MUStat-usersPerModule', label:'label', borderType:'NONE', preferredHeight:300, preferredWidth:300 ) {

                    pieSeries (valueField:'count')
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
                      propertyView name:'accessCount', readOnly:true
                    }
                  }
                }
                center {
                  polarChart (model:'MUStat-accessPerModule', label:'label', borderType:'NONE', preferredHeight:300, preferredWidth:300) {

                    pieSeries (valueField:'count')
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
                referencePropertyView name:'historyPeriod', lovAction:'lovFindFrontAction'
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
