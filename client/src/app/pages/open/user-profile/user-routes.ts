import {Routes} from "@angular/router";
import {UserProfileComponent} from "./user-profile.component";


export const USER_ROUTES: Routes = [

  {
    path: ':id',
    component: UserProfileComponent
  },
];
