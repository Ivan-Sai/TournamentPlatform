import {Component, Input, OnInit} from '@angular/core';
import {BracketService} from "../../../../../services/BracketService";
import {BracketData} from "../../../../../models/request/BracketData";
import {NgForOf, NgIf, NgStyle} from "@angular/common";
import {StageData} from "../../../../../models/request/StageData";
import {last} from "rxjs";
import {JwtUtil} from "../../../../../util/JwtUtil";
import {TeamService} from "../../../../../services/TeamService";
import {TeamData} from "../../../../../models/request/TeamData";
import {Router} from "@angular/router";
import {BracketRefreshService} from "../../../../../services/BracketRefreshService";

@Component({
  selector: 'app-bracket',
  standalone: true,
  templateUrl: './bracket.component.html',
  imports: [
    NgIf,
    NgForOf,
    NgStyle
  ],
  styleUrls: ['./bracket.component.css']
})
export class BracketComponent implements OnInit{
  @Input() bracketId: number | undefined;
  @Input() adminId: number | undefined;
  @Input() winnerId: number | undefined;

  public bracket: BracketData | undefined;
  public role: string | null = this.jwtUtil.getRole();
  public userId: number | undefined = this.jwtUtil.getId();
  public winner: TeamData | undefined;

  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: { [key: number]: boolean } = {}
  public teams: TeamData[] | undefined;
  constructor(private bracketService: BracketService,
              private jwtUtil: JwtUtil,
              private teamService: TeamService,
              private router: Router,
              private bracketRefreshService: BracketRefreshService
              ) {
  }
  ngOnInit(): void {
    this.bracketRefreshService.bracketRefresh$.subscribe(() => {
      this.bracketService.findBracket(this.bracketId).subscribe(value => {
        this.bracket = value;
      })
    });
    this.bracketService.findBracket(this.bracketId).subscribe(value => {
      this.bracket = value;
      this.teams = [];

      value.stages.forEach(stage => {
        stage.matches.forEach(match => {
          this.teams?.push(match.team1);
          this.teams?.push(match.team2);
        });
      });

      this.loadLogos();
    });

    if (this.winnerId != 0) {
      this.teamService.findById(this.winnerId).subscribe(value => {
        this.winner = value;
      });
    }
  }



  loadLogos(): void {
    if (this.teams)
    this.teams.forEach(value => {
        this.teamService.getTeamLogo(value.id).subscribe(
          value2 => {
            this.logoDataMap[value.id] = 'data:image/jpeg;base64,' + value2.data
            this.isHasLogoMap[value.id] = true;
          }
          , error => {
            this.isHasLogoMap[value.id] = false
          }
        )
      })
  }

  getLogo(id: number): string {
    return this.logoDataMap[id]
  }

  teamWon(teamId: number, bracketId: number, matchId: number | undefined, stageId: number) {
    this.bracketService.teamWon(teamId,matchId,bracketId,stageId).subscribe(
      () => {
        this.bracketRefreshService.refreshBracket();
      }
    );
  }

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }
  navigateToTeamPage(id: number | undefined) {
    this.router.navigateByUrl('teams/' + id);
  }
}
