// Global type declarations to resolve MUI and other library issues

// MUI Material Components
declare module '@mui/material' {
  export const AppBar: any;
  export const Toolbar: any;
  export const Typography: any;
  export const Button: any;
  export const Box: any;
  export const IconButton: any;
  export const Menu: any;
  export const MenuItem: any;
  export const Avatar: any;
  export const Container: any;
  export const useMediaQuery: any;
  export const useTheme: any;
  export const Drawer: any;
  export const List: any;
  export const ListItem: any;
  export const ListItemIcon: any;
  export const ListItemText: any;
  export const Divider: any;
  export const CssBaseline: any;
  export const CircularProgress: any;
  export const Paper: any;
  export const Grid: any;
  export const Rating: any;
  export const LinearProgress: any;
  export const TextField: any;
  export const Alert: any;
  export const Dialog: any;
  export const DialogTitle: any;
  export const DialogContent: any;
  export const DialogActions: any;
  export const Card: any;
  export const CardContent: any;
  export const CardMedia: any;
  export const CardActions: any;
  export const FormControl: any;
  export const InputLabel: any;
  export const Select: any;
  export const Table: any;
  export const TableBody: any;
  export const TableCell: any;
  export const TableContainer: any;
  export const TableHead: any;
  export const TableRow: any;
  export const Chip: any;
  export const Tabs: any;
  export const Tab: any;
  export const Fab: any;
  export const InputAdornment: any;
}

// MUI Material Styles
declare module '@mui/material/styles' {
  export const ThemeProvider: any;
  export const createTheme: any;
  export const useTheme: any;
  
  interface Palette {
    surface?: any;
  }

  interface PaletteOptions {
    surface?: any;
  }
}

// MUI Icons
declare module '@mui/icons-material' {
  export const Menu: any;
  export const AccountCircle: any;
  export const Movie: any;
  export const BookOnline: any;
  export const Dashboard: any;
  export const Logout: any;
  export const Home: any;
  export const Person: any;
  export const Star: any;
  export const MoreVert: any;
  export const Edit: any;
  export const Delete: any;
  export const Add: any;
  export const People: any;
  export const Assessment: any;
  export const Schedule: any;
  export const AttachMoney: any;
  export const Visibility: any;
  export const QrCodeScanner: any;
  export const TrendingUp: any;
  export const DateRange: any;
  export const Download: any;
  export const CheckCircle: any;
  export const EventSeat: any;
  export const VisibilityOff: any;
  export const Email: any;
  export const Lock: any;
  export const Phone: any;
  export const CreditCard: any;
  export const Security: any;
  export const PlayArrow: any;
  export const AccessTime: any;
  export const Language: any;
  export const CalendarToday: any;
  export const Search: any;
  export const Save: any;
  export const Cancel: any;
}
