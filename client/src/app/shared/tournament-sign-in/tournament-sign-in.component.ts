import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {TournamentSignInService} from "../../services/TournamentSigninService";
import {PersonalService} from "../../services/PersonalService";
import {TeamService} from "../../services/TeamService";
import {JwtUtil} from "../../util/JwtUtil";
import {Router} from "@angular/router";


@Component({
  selector: 'app-tournament-sign-in',
  templateUrl: './tournament-sign-in.component.html',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    AsyncPipe
  ],
  styleUrls: ['./tournament-sign-in.component.css']
})
export class TournamentSignInComponent implements OnInit{
  tournamentSignupWindowOpen = false;
  teams$ = this.personalService.getTeamsByPersonal(this.jwtUtil.getId());
  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: {[key: number] : boolean} = {}


  constructor(public tournamentSignInService: TournamentSignInService,
              private personalService: PersonalService,
              private teamService: TeamService,
              private jwtUtil:JwtUtil,
              private router: Router,
              ) {

    this.tournamentSignInService.tournamentSignupWindowOpen$.subscribe((value) => {
      this.tournamentSignupWindowOpen = value;
    });
  }



  ngOnInit(): void {
        this.loadLogos()
    }

  loadLogos(): void {
    this.teams$.forEach(value => {
      value.forEach(value1 => {
        this.teamService.getTeamLogo(value1.id).subscribe(
          value2 => {
            this.logoDataMap[value1.id] = 'data:image/jpeg;base64,' + value2.data
            this.isHasLogoMap[value1.id] = true;
          }
          ,error => {
            this.isHasLogoMap[value1.id] = false
          }
        )
      })
    })

  }
  getLogo(id: number): string {
    return this.logoDataMap[id]
  }

  redirectToPage(url:string){
    this.router.navigateByUrl(url);
  }

}
