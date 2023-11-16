import {Routes} from "@angular/router";
import {Error404Component} from "./error404/error404.component";
import {ErrorLinkComponent} from "./error-link/error-link.component";



export const ERROR_ROUTES: Routes = [

  {
    path: '404',
    component: Error404Component
  },
  {
    path: 'link',
    component: ErrorLinkComponent
  },

];
