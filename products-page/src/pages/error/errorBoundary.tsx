import React, { ReactNode } from "react";

type BoundaryProps = {
    fallback : any;
    children : ReactNode | undefined;
};
type BoundaryState = {
    hasError: boolean;
};
export class ErrorBoundary extends React.Component<BoundaryProps, BoundaryState> {
    constructor(props:any) {
        super(props);
        this.state = { hasError: false };
    }
    static getDerivedStateFromError(error:any) {
        return { hasError: true };
    }
    render() {
        if (this.state.hasError) {
            return this.props.fallback;
        }
        return this.props.children;
    }
}